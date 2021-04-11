package com.wyvernrunner.wicket.pageclasses;

import com.wyvernrunner.wicket.simulator.Hero;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.List;

public class HeroForm extends Form {

    //Fields Initializing

    //Tank
    private double attackTank;
    private double healthTank;
    private double defenseTank;
    private double speedTank;
    private float ccTank;
    private int cdmgTank;
    private int effTank;
    private int effresTank;
    private int dualTank;

    //Hero 1
    private double attackH1;
    private double healthH1;
    private double defenseH1;
    private double speedH1;
    private float ccH1;
    private int cdmgH1;
    private int effH1;
    private int effresH1;
    private int dualH1;

    //Hero 2
    private double attackH2;
    private double healthH2;
    private double defenseH2;
    private double speedH2;
    private float ccH2;
    private int cdmgH2;
    private int effH2;
    private int effresH2;
    private int dualH2;

    //Hero 3
    private double attackH3;
    private double healthH3;
    private double defenseH3;
    private double speedH3;
    private float ccH3;
    private int cdmgH3;
    private int effH3;
    private int effresH3;
    private int dualH3;

    //Form function
    public HeroForm(String id){
        super(id);
        setDefaultModel(new CompoundPropertyModel(this));

        //Initialisation of form fields

        //Tank
        add(new TextField("attackTank"));
        add(new TextField("healthTank"));
        add(new TextField("defenseTank"));
        add(new TextField("speedTank"));
        add(new TextField("ccTank"));
        add(new TextField("cdmgTank"));
        add(new TextField("effTank"));
        add(new TextField("effresTank"));
        add(new TextField("dualTank"));

        //Hero 1
        add(new TextField("attackH1"));
        add(new TextField("healthH1"));
        add(new TextField("defenseH1"));
        add(new TextField("speedH1"));
        add(new TextField("ccH1"));
        add(new TextField("cdmgH1"));
        add(new TextField("effH1"));
        add(new TextField("effresH1"));
        add(new TextField("dualH1"));

        //Hero 2
        add(new TextField("attackH2"));
        add(new TextField("healthH2"));
        add(new TextField("defenseH2"));
        add(new TextField("speedH2"));
        add(new TextField("ccH2"));
        add(new TextField("cdmgH2"));
        add(new TextField("effH2"));
        add(new TextField("effresH2"));
        add(new TextField("dualH2"));

        //Hero 3
        add(new TextField("attackH3"));
        add(new TextField("healthH3"));
        add(new TextField("defenseH3"));
        add(new TextField("speedH3"));
        add(new TextField("ccH3"));
        add(new TextField("cdmgH3"));
        add(new TextField("effH3"));
        add(new TextField("effresH3"));
        add(new TextField("dualH3"));
    }

    public final void onSubmit(){
        //execute sim with user stats
        //todo: Make form field verifications (tests)


    }





}
