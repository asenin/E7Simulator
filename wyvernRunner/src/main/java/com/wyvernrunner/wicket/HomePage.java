package com.wyvernrunner.wicket;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.WebPage;

import java.util.List;

public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;
	private Form form;
	private DropDownChoice<Hero> heroList;

	public HomePage(final PageParameters parameters) {
		super(parameters);

		Model<Hero> listModel = new Model<Hero>();
		ChoiceRenderer<Hero> heroRender = new ChoiceRenderer<Hero>("fullName");

		add(new Label("version", getApplication().getFrameworkSettings().getVersion()));


		heroList = new DropDownChoice<Hero>("persons", listModel, loadHeroes(),
				heroRender){

			protected boolean wantOnSelectionChangedNotifications() {
				return true;
			}
		};

		add(heroList);

		form = new Form("form", new CompoundPropertyModel<Hero>(listModel)); //Form initializing

		//Tank
		form.add(new TextField("attackTank"));
		form.add(new TextField("defenseTank"));
		form.add(new TextField("speedTank"));
		form.add(new TextField("ccTank"));
		form.add(new TextField("cdmgTank"));
		form.add(new TextField("effTank"));
		form.add(new TextField("effresTank"));
		form.add(new TextField("dualTank"));

		//Hero 1
		form.add(new TextField("attackH1"));
		form.add(new TextField("defenseH1"));
		form.add(new TextField("speedH1"));
		form.add(new TextField("ccH1"));
		form.add(new TextField("cdmgH1"));
		form.add(new TextField("effH1"));
		form.add(new TextField("effresH1"));
		form.add(new TextField("dualH1"));

		//Hero 2
		form.add(new TextField("attackH2"));
		form.add(new TextField("defenseH2"));
		form.add(new TextField("speedH2"));
		form.add(new TextField("ccH2"));
		form.add(new TextField("cdmgH2"));
		form.add(new TextField("effH2"));
		form.add(new TextField("effresH2"));
		form.add(new TextField("dualH2"));

		//Hero 3
		form.add(new TextField("attackH3"));
		form.add(new TextField("defenseH3"));
		form.add(new TextField("speedH3"));
		form.add(new TextField("ccH3"));
		form.add(new TextField("cdmgH3"));
		form.add(new TextField("effH3"));
		form.add(new TextField("effresH3"));
		form.add(new TextField("dualH3"));

		add(form);
	}

	private IModel<? extends List<? extends Hero>> loadHeroes() {
		return null;
	}

}

