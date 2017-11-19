package de.tub.ise.anwsys.models;

import de.tub.ise.anwsys.repos.PizzaRepository;
import de.tub.ise.anwsys.repos.ToppingRepository;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public final class Pizza implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6009190778756332608L;

	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	Integer id;
	
	@NotNull
	String name;
	

	Float price;
	
	
	public enum Size
	{
	
		Standard, Large

	}
	@NotNull
	Size size;
	
	
	ArrayList<Integer> ToppingIds = new ArrayList<Integer> ();
	
	
	public Integer getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	public Float getPrice() {



		return price;
	}
 
	
	
	
	public Size getSize() {
		return size;
	}
	
//	public void setId(Integer id) {
//		this.id = id;
//	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPrice(Float price) {
		this.price = price;
	}
	
	 
	public void setSize(Size size) {
		
		this.size=size;
		
	}
	
	public ArrayList<Integer> getToppingIds() {
		return ToppingIds;
	}
	
	public void setToppingIds(ArrayList<Integer> toppingIds)
	{
		ToppingIds = toppingIds;
	}
	 
	
	public Pizza( String name, Size size )
	{
	this.name=name;
	//this.id=id;

	if(size.equals(Size.Standard))
	{
		this.price=(float)5.0;

	}
	else if(size.equals(Size.Large))
	{
		this.price=(float)8.5;
		}

	this.size=size;
	}

	 
	
	public Pizza() {}
	
	

}
