package cs125.healthhelper;


import java.io.Serializable;

//basic Food class that stores info on a meal
public class Food implements Serializable {	//is Serializable so it can be put inside an Intent
	
	public int ndbno;		//id number for the food
	public String name;
	//date consumed?

	
	//base nutrient amount, the amount you will get per 100grams of the food
	private float bcals;
	private float bcarbs;
	private float bprotein;
	private float bfat;
	
	//the nutrients you will get, given the number of grams of the food
	public float grams;	
	public float calories;
	public float carbs;
	public float protein;
	public float fat;

	//Constructs Food with the name and ndbno
	//ndbno is used to find info on the food in database
	public Food(String n, int id) {
		ndbno = id;
		name = n.trim();
		if(name.endsWith(","))
			name = name.substring(0,name.length()-1);
		
		bcals = 0;
		bcarbs = 0;
		bprotein = 0;
		bfat = 0;
		
		grams = 0;
		calories = 0;
		protein = 0;
		carbs = 0;
		fat = 0;
	}

	public Food(String n, int grams, int calories, int carbs, int protein, int fat)
	{
		this.name = n;
		this.ndbno = -1;

		this.grams = grams;
		this.calories = calories;
		this.carbs = carbs;
		this.protein = protein;
		this.fat = fat;
	}

	@Override
	public String toString(){
		return name;
	}


	//sets the grams of food consumed
	//updates the nutrients based on the grams of food
	public void amountConsumed(float grams) {
		this.grams = grams;
		calories = bcals*grams/100;
		protein = bprotein*grams/100;
		carbs = bcarbs*grams/100;
		fat = bfat*grams/100;
	}
	
	
	
	//basic setters and getters

	public float getBcals() {
		return bcals;
	}

	public void setBcals(float bcals) {
		this.bcals = bcals;
	}

	public float getBcarbs() {
		return bcarbs;
	}

	public void setBcarbs(float bcarbs) {
		this.bcarbs = bcarbs;
	}

	public float getBprotein() {
		return bprotein;
	}

	public void setBprotein(float bprotein) {
		this.bprotein = bprotein;
	}

	public float getBfat() {
		return bfat;
	}

	public void setBfat(float bfat) {
		this.bfat = bfat;
	}

	
}
