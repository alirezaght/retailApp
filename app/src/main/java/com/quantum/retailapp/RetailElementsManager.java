package com.quantum.retailapp;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class RetailElementsManager {

    private static RetailElementsManager _defaultInstance;

    private List<RetailElement> elements;

    public static RetailElementsManager getDefaultInstance() {
        if (_defaultInstance == null)
            _defaultInstance = new RetailElementsManager();

        return _defaultInstance;
    }

    RetailElementsManager() {
        elements = new ArrayList<>();

        RetailElement element = new RetailElement();
        element.setAgreementType(RetailElement.AgreementType.RENT);
        element.setArea(140);
        element.setFloorNo("دوم");
        element.setRegion("گیشا");
        element.setAddress("خیابان نهم. پلاک 14");
        element.setBuildingAge(5);
        element.setMortgageMoney(20000000);
        element.setRentMoney(1000000);
        element.setOwnerName("علیزاده");
        element.setPhoneNo("+989121234567");
        element.setOtherSpecs(new String[]{"پارکینگ", "آسانسور", "آشپزخانه اوپن"});
        element.setRoomsCount(3);
        element.setBuildingType(RetailElement.BuildingType.Apartman);
        element.setId(1);
        elements.add(element);

        element = new RetailElement();
        element.setAgreementType(RetailElement.AgreementType.RENT);
        element.setArea(110);
        element.setFloorNo("اول");
        element.setRegion("ستارخان");
        element.setAddress("خیابان باقرخان. پلاک 23");
        element.setBuildingAge(15);
        element.setMortgageMoney(25000000);
        element.setRentMoney(8000000);
        element.setOwnerName("محمدی");
        element.setPhoneNo("+989129876543");
        element.setOtherSpecs(new String[]{"پارکینگ", "باکن", "آشپزخانه اوپن"});
        element.setRoomsCount(2);
        element.setBuildingType(RetailElement.BuildingType.Apartman);

        element.setId(2);
        elements.add(element);

        element = new RetailElement();
        element.setAgreementType(RetailElement.AgreementType.SELL);
        element.setArea(90);
        element.setFloorNo("اول");
        element.setRegion("سعادت آباد");
        element.setAddress("خیابان 36، پلاک 12، واحد 4");
        element.setBuildingAge(15);
        element.setMortgageMoney(18000000);
        element.setRentMoney(5500000);
        element.setOwnerName("مقسمی");
        element.setPhoneNo("+989129876543");
        element.setOtherSpecs(new String[]{"باکن", "آشپزخانه اوپن"});
        element.setRoomsCount(2);
        element.setBuildingType(RetailElement.BuildingType.Apartman);
        element.setId(3);
        elements.add(element);

        element = new RetailElement();
        element.setAgreementType(RetailElement.AgreementType.SELL);
        element.setArea(100);
        element.setFloorNo("اول");
        element.setRegion("پونک");
        element.setAddress("خیابان خوش طینت، بزرگراه اشرفی، ساختمان نور، واحد 9");
        element.setBuildingAge(15);
        element.setMortgageMoney(35000000);
        element.setRentMoney(7000000);
        element.setOwnerName("مقدم");
        element.setPhoneNo("+989129876543");
        element.setOtherSpecs(new String[]{"پارکینگ", "دو خط تلفن", "باکن", "آشپزخانه اوپن"});
        element.setRoomsCount(3);
        element.setBuildingType(RetailElement.BuildingType.Apartman);
        element.setId(4);
        elements.add(element);


        element = new RetailElement();
        element.setAgreementType(RetailElement.AgreementType.SELL);
        element.setArea(180);
        element.setFloorNo("");
        element.setRegion("پونک");
        element.setAddress("خیابان خوش طینت، بزرگراه اشرفی، ساختمان نور، واحد 9");
        element.setBuildingAge(23);
        element.setTotalCost(800000000);
        element.setOwnerName("مقدم");
        element.setPhoneNo("+989129876543");
        element.setOtherSpecs(new String[]{});
        element.setRoomsCount(3);
        element.setBuildingType(RetailElement.BuildingType.Zamin);
        element.setId(5);
        elements.add(element);


        element = new RetailElement();
        element.setAgreementType(RetailElement.AgreementType.SELL);
        element.setArea(250);
        element.setFloorNo("");
        element.setRegion("پونک");
        element.setAddress("بزرگراه رسالت، خیابان مدنی، پلاک2، واحد2");
        element.setBuildingAge(23);
        element.setTotalCost(800000000);
        element.setOwnerName("مقدم");
        element.setPhoneNo("+989129876543");
        element.setOtherSpecs(new String[]{});
        element.setRoomsCount(3);
        element.setBuildingType(RetailElement.BuildingType.Zamin);
        element.setId(6);
        elements.add(element);


    }

    public void RefreshListFromFile(String fileName, byte[] key, byte[] IVkey) {
        File root = Environment.getExternalStorageDirectory();
        File fileDir = new File(root.getAbsolutePath());

        File file = new File(fileDir, fileName);

        try {
            InputStream stream = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            stream.read(buffer);

            byte[] data = buffer;
//            byte[] data = ServiceHandler.decrypt(key, IVkey, buffer);
            InputStream is = new ByteArrayInputStream(data);
            GZIPInputStream zis = new GZIPInputStream(new BufferedInputStream(is));
            String jsonValue;
            int id = 1;
            try {
                jsonValue = ServiceHandler.convertInputStreamToString(zis);

                JSONObject rootObj = new JSONObject(jsonValue);
                elements.clear();
                JSONArray rentApartments = rootObj.getJSONArray("Apartments-Rent");
                for (int i = 0; i < rentApartments.length(); i++) {
                    JSONObject obj = rentApartments.getJSONObject(i);
                    RetailElement element = new RetailElement();
                    element.setAgreementType(RetailElement.AgreementType.RENT);
                    element.setArea(obj.getInt("AR"));
                    element.setFloorNo(obj.getString("FL"));
                    element.setAddress(obj.getString("AD"));
                    element.setBuildingAge(obj.getInt("AG"));
                    element.setMortgageMoney(obj.getLong("MT"));
                    element.setRentMoney(obj.getLong("RV"));
                    element.setOwnerName(obj.getString("OW"));
                    element.setPhoneNo(obj.getJSONArray("PH").getString(0));
                    element.setRoomsCount(obj.getInt("NR"));
                    element.setBuildingType(RetailElement.BuildingType.Apartman);
                    element.setId(id++);
                    elements.add(element);
                }
                JSONArray saleApartments = rootObj.getJSONArray("Apartments-Sale");
                for (int i = 0; i < saleApartments.length(); i++) {
                    JSONObject obj = saleApartments.getJSONObject(i);
                    RetailElement element = new RetailElement();
                    element.setAgreementType(RetailElement.AgreementType.SELL);
                    element.setArea(obj.getInt("AR"));
                    element.setFloorNo(obj.getString("FL"));
                    element.setAddress(obj.getString("AD"));
                    element.setBuildingAge(obj.getInt("AG"));
                    element.setMortgageMoney(obj.getLong("MT"));
                    element.setMeterCost(obj.getLong("MC"));
                    element.setTotalCost(obj.getLong("TC"));
                    element.setPhoneNo(obj.getJSONArray("PH").getString(0));
                    element.setRoomsCount(obj.getInt("NR"));
                    element.setBuildingType(RetailElement.BuildingType.Apartman);
                    element.setId(id++);
                    elements.add(element);
                }
                JSONArray rentMostaghelat = rootObj.getJSONArray("Mostaghelat-Rent");
                for (int i = 0; i < rentMostaghelat.length(); i++) {
                    JSONObject obj = rentMostaghelat.getJSONObject(i);
                    RetailElement element = new RetailElement();
                    element.setAgreementType(RetailElement.AgreementType.RENT);
                    element.setArea(obj.getInt("AR"));
                    element.setFloorNo(obj.getString("FL"));
                    element.setAddress(obj.getString("AD"));
                    element.setBuildingAge(obj.getInt("AG"));
                    element.setMortgageMoney(obj.getLong("MT"));
                    element.setRentMoney(obj.getLong("RV"));
                    element.setOwnerName(obj.getString("OW"));
                    element.setPhoneNo(obj.getJSONArray("PH").getString(0));
                    element.setBuildingType(RetailElement.BuildingType.Mostaghelat);
                    element.setId(id++);
                    elements.add(element);
                }
                JSONArray saleMostaghelat = rootObj.getJSONArray("Mostaghelat-Sale");
                for (int i = 0; i < saleMostaghelat.length(); i++) {
                    JSONObject obj = saleMostaghelat.getJSONObject(i);
                    RetailElement element = new RetailElement();
                    element.setAgreementType(RetailElement.AgreementType.SELL);
                    element.setArea(obj.getInt("AR"));
                    element.setFloorNo(obj.getString("FL"));
                    element.setAddress(obj.getString("AD"));
                    element.setBuildingAge(obj.getInt("AG"));
                    element.setMeterCost(obj.getLong("MC"));
                    element.setTotalCost(obj.getLong("TC"));
                    element.setOwnerName(obj.getString("OW"));
                    element.setPhoneNo(obj.getJSONArray("PH").getString(0));
                    element.setBuildingType(RetailElement.BuildingType.Mostaghelat);
                    element.setId(id++);
                    elements.add(element);
                }
                JSONArray rentMaghaze = rootObj.getJSONArray("Maghaze-Rent");
                for (int i = 0; i < rentMaghaze.length(); i++) {
                    JSONObject obj = rentMaghaze.getJSONObject(i);
                    RetailElement element = new RetailElement();
                    element.setAgreementType(RetailElement.AgreementType.RENT);
                    element.setArea(obj.getInt("AR"));
                    element.setFloorNo(obj.getString("FL"));
                    element.setAddress(obj.getString("AD"));
                    element.setBuildingAge(obj.getInt("AG"));
                    element.setMortgageMoney(obj.getLong("MT"));
                    element.setRentMoney(obj.getLong("RV"));
                    element.setOwnerName(obj.getString("OW"));
                    element.setPhoneNo(obj.getJSONArray("PH").getString(0));
                    element.setBuildingType(RetailElement.BuildingType.Maghaze);
                    element.setId(id++);
                    elements.add(element);
                }
                JSONArray saleMaghaze = rootObj.getJSONArray("Maghaze-Sale");
                for (int i = 0; i < saleMaghaze.length(); i++) {
                    JSONObject obj = saleMaghaze.getJSONObject(i);
                    RetailElement element = new RetailElement();
                    element.setAgreementType(RetailElement.AgreementType.SELL);
                    element.setArea(obj.getInt("AR"));
                    element.setFloorNo(obj.getString("FL"));
                    element.setAddress(obj.getString("AD"));
                    element.setBuildingAge(obj.getInt("AG"));
                    element.setMeterCost(obj.getLong("MC"));
                    element.setTotalCost(obj.getLong("TC"));
                    element.setOwnerName(obj.getString("OW"));
                    element.setPhoneNo(obj.getJSONArray("PH").getString(0));
                    element.setBuildingType(RetailElement.BuildingType.Maghaze);
                    element.setId(id++);
                    elements.add(element);
                }
                JSONArray rentVila = rootObj.getJSONArray("Vila-Rent");
                for (int i = 0; i < rentVila.length(); i++) {
                    JSONObject obj = rentVila.getJSONObject(i);
                    RetailElement element = new RetailElement();
                    element.setAgreementType(RetailElement.AgreementType.RENT);
                    element.setArea(obj.getInt("AR"));
                    element.setFloorNo(obj.getString("FL"));
                    element.setAddress(obj.getString("AD"));
                    element.setBuildingAge(obj.getInt("AG"));
                    element.setMortgageMoney(obj.getLong("MT"));
                    element.setRentMoney(obj.getLong("RV"));
                    element.setOwnerName(obj.getString("OW"));
                    element.setPhoneNo(obj.getJSONArray("PH").getString(0));
                    element.setBuildingType(RetailElement.BuildingType.Vila);
                    element.setId(id++);
                    elements.add(element);
                }
                JSONArray saleVila = rootObj.getJSONArray("Vila-Sale");
                for (int i = 0; i < saleVila.length(); i++) {
                    JSONObject obj = saleVila.getJSONObject(i);
                    RetailElement element = new RetailElement();
                    element.setAgreementType(RetailElement.AgreementType.SELL);
                    element.setArea(obj.getInt("AR"));
                    element.setAddress(obj.getString("AD"));
                    element.setBuildingAge(obj.getInt("AG"));
                    element.setMeterCost(obj.getLong("MC"));
                    element.setTotalCost(obj.getLong("TC"));
                    element.setOwnerName(obj.getString("OW"));
                    element.setPhoneNo(obj.getJSONArray("PH").getString(0));
                    element.setBuildingType(RetailElement.BuildingType.Vila);
                    element.setId(id++);
                    elements.add(element);
                }
                JSONArray saleZamin = rootObj.getJSONArray("Zamin-Sale");
                for (int i = 0; i < saleZamin.length(); i++) {
                    JSONObject obj = saleZamin.getJSONObject(i);
                    RetailElement element = new RetailElement();
                    element.setAgreementType(RetailElement.AgreementType.SELL);
                    element.setArea(obj.getInt("AR"));
                    element.setAddress(obj.getString("AD"));
                    element.setMeterCost(obj.getLong("MC"));
                    element.setTotalCost(obj.getLong("TC"));
                    element.setOwnerName(obj.getString("OW"));
                    element.setPhoneNo(obj.getJSONArray("PH").getString(0));
                    element.setBuildingType(RetailElement.BuildingType.Zamin);
                    element.setId(id++);
                    elements.add(element);
                }

            } finally {
                zis.close();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public List<RetailElement> getList() {
        return elements;
    }

    public RetailElement getElementById(int id) {
        for (RetailElement element : elements)
            if (element.getId() == id)
                return element;


//        if (id >= 0 && id < elements.size())
//            return elements.get(id);
        return null;
    }

    public class RetailElementsAdapter extends BaseAdapter {
        RetailElement.BuildingType _buildingType;
        private Context _context;

        public RetailElementsAdapter(Context context, RetailElement.BuildingType buidlingType) {
            _buildingType = buidlingType;
            _context = context;
        }

        public ArrayList getAllItems() {
            ArrayList tmp = new ArrayList();
            for (RetailElement element : RetailElementsManager.getDefaultInstance().getList())
                if (element.getBuildingType() == _buildingType)
                    tmp.add(element);
            return tmp;
        }

        @Override
        public int getCount() {
            int count = 0;
            for (RetailElement element : RetailElementsManager.getDefaultInstance().getList())
                if (element.getBuildingType() == _buildingType)
                    count++;


            return count;
        }

        @Override
        public Object getItem(int position) {
            int count = -1;
            for (RetailElement element : RetailElementsManager.getDefaultInstance().getList()) {
                if (element.getBuildingType() == _buildingType)
                    count++;
                else
                    continue;

                if (count == position)
                    return element;
            }

            return null;
        }

        public RetailElement.BuildingType getBuidlingType() {
            return _buildingType;
        }

        @Override
        public long getItemId(int position) {
            return getCount();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rootView = inflater.inflate(android.R.layout.two_line_list_item, parent, false);
            TextView title = (TextView) rootView.findViewById(android.R.id.text1);
            TextView region = (TextView) rootView.findViewById(android.R.id.text2);
            RetailElement retailElement = (RetailElement) getItem(position);
            rootView.setMinimumWidth(48);
            title.setPadding(5, 5, 5, 5);
            region.setPadding(5, 5, 5, 5);
            if (retailElement != null) {
                title.setText(retailElement.getTitleString());
                region.setText(retailElement.getBodyDetailString());
            }

            return rootView;
        }
    }
}
