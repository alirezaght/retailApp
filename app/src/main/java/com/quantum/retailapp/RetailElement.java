package com.quantum.retailapp;

import android.graphics.Color;

/**
 * Created by RaptoR on 12/19/2014.
 */
public class RetailElement {

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public long getMortgageMoney() {
        return mortgageMoney;
    }

    public void setMortgageMoney(long mortgageMoney) {
        this.mortgageMoney = mortgageMoney;
    }

    public int getRoomsCount() {
        return roomsCount;
    }

    public void setRoomsCount(int roomsCount) {
        this.roomsCount = roomsCount;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String[] getOtherSpecs() {
        return otherSpecs;
    }

    public void setOtherSpecs(String[] otherSpecs) {
        this.otherSpecs = otherSpecs;
    }

    public int getBuildingAge() {
        return buildingAge;
    }

    public void setBuildingAge(int buildingAge) {
        this.buildingAge = buildingAge;
    }

    public String getFloorNo() {
        return floorNo;
    }

    public void setFloorNo(String floorNo) {
        this.floorNo = floorNo;
    }

    public long getRentMoney() {
        return rentMoney;
    }

    public void setRentMoney(long rentMoney) {
        this.rentMoney = rentMoney;
    }

    public long getMeterCost() {
        return meterCost;
    }

    public void setMeterCost(long meterCost) {
        this.meterCost = meterCost;
    }

    public long getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(long totalCost) {
        this.totalCost = totalCost;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }

    public static final String[] BuldingTypes = {"آپارتمان", "مغازه", "مستقلات", "ویلا", "زمین"};
    public static final String[] AgreementTypes = {"اجاره", "فروشی"};

    public enum BuildingType {
        Apartman,
        Maghaze,
        Mostaghelat,
        Vila,
        Zamin,
    }

    public enum AgreementType {
        RENT,//اجاره
        SELL,//فروشی
    }

    private static final int[] ElementTypeColor = {Color.rgb(153, 239, 255), Color.GREEN, Color.CYAN, Color.LTGRAY};

    private int id;
    private String ownerName;
    private String address;
    private String region;
    private String phoneNo;
    private int area;
    private int roomsCount;
    private long mortgageMoney;
    private long rentMoney;
    private long meterCost;
    private long totalCost;
    private String[] otherSpecs;
    private String floorNo;
    private int buildingAge;
    private AgreementType agreementType;
    private BuildingType buildingType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }


    public AgreementType getAgreementType() {
        return agreementType;
    }

    public void setAgreementType(AgreementType agreementType) {
        this.agreementType = agreementType;
    }

    public String getTitleString() {
        String retValue = BuldingTypes[getBuildingType().ordinal()];
        switch (getBuildingType()) {
            case Apartman:
                retValue += " " + getBuildingAge() + "ساله - طبقه " + getFloorNo() + getRegion();
                break;
            case Maghaze:
                retValue += " " + getBuildingAge() + "س ساله " + getRegion();
                break;
            case Mostaghelat:
                retValue += " " + getBuildingAge() + " ساله " + getRegion();
                break;
            case Vila:
                retValue += " " + getBuildingAge() + " ساله " + getRegion();
                break;
            case Zamin:
                retValue += getRegion();
                break;
        }
        retValue += "  " + getArea() + " متر";
        return retValue;
    }

    public String getBodyDetailString() {
        switch (getAgreementType()) {
            case RENT:
                return "رهن : " + getMortgageMoney() + " - اجاره :" + getRentMoney();
            case SELL:
                return "قیمت هر متر مربع : " + getMeterCost() + "\r\n" + "قیمت کل : " + getTotalCost();
        }
        return null;
    }

    public String getOtherSpecsString() {
        if (otherSpecs == null)
            return "";
        String retValue = "";
        for (String item : otherSpecs)
            retValue += item + " - ";
        retValue = retValue.substring(0, retValue.length() - 1);
        return retValue;
    }


}
