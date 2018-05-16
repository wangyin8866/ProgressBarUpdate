package com.wyman.myapplication;

/**
 * @author wyman
 * @date 2018/4/24
 * description :
 */

public class DataModel {
    private Double amoutMomeny;
    private Double investMoney;
    private int dotColor;
    private int textColor;
    private String mtext;

    public DataModel(Double amoutMomeny, Double investMoney) {
        this.amoutMomeny = amoutMomeny;
        this.investMoney = investMoney;
    }

    public void setDotColor(int dotColor) {
        this.dotColor = dotColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setMtext(String mtext) {
        this.mtext = mtext;
    }

    public void setAmoutMomeny(Double amoutMomeny) {
        this.amoutMomeny = amoutMomeny;
    }

    public void setInvestMoney(Double investMoney) {
        this.investMoney = investMoney;
    }
}
