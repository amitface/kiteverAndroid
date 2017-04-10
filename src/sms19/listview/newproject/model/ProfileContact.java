package sms19.listview.newproject.model;

import java.util.List;



public class ProfileContact {

    private List<CityList> CityList;
    private List<StateList> StateList ;

    public List<CityList> getCityList() {
        return CityList;
    }

    public void setCityList(List<CityList> cityList) {
        this.CityList = cityList;
    }


    public List<StateList> getStateList() {
        return StateList;
    }

    public void setStateList(List<StateList> stateList) {
        this.StateList = stateList;
    }

}
