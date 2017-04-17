package com.headwire.asbp.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

/**
 * Created by headwire on 4/17/2017.
 */
@Model(adaptables=Resource.class)
public class TestingComponent {

    @Inject @Default(values="First Text")
    private String firstText;

    @Inject @Default(values="Second Text")
    private String secondText;

    public String getFirstText() {
        return firstText;
    }

    public void setFirstText(String firstText) {
        this.firstText = firstText;
    }

    public String getSecondText() {
        return secondText;
    }

    public void setSecondText(String secondText) {
        this.secondText = secondText;
    }
}
