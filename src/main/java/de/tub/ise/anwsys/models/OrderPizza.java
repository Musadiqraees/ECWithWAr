package de.tub.ise.anwsys.models;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;




@Entity
public class OrderPizza implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6009190778756332608L;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Float getTotalPrice() {
		return totalPrice;
	}

	@Override
	public String toString() {
		return "OrderPizza{" +
				"id=" + id +
				", totalPrice=" + totalPrice +
				", recipient='" + recipient + '\'' +
				", orderItems=" + orderItems +
				'}';
	}

	public void setTotalPrice(Float totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public ArrayList<orderPizzaItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(ArrayList<orderPizzaItem> orderItems) {
		this.orderItems = orderItems;
	}

	public OrderPizza(Float totalPrice, String recipient, ArrayList<orderPizzaItem> orderItems) {

		this.totalPrice = totalPrice;
		this.recipient = recipient;
		this.orderItems = orderItems;


	}



	protected OrderPizza(){

		
	}


@Id
@GeneratedValue(strategy=GenerationType.AUTO)
int id;

Float totalPrice;

String recipient;

ArrayList<orderPizzaItem> orderItems;

}
