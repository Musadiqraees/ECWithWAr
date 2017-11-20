package de.tub.ise.anwsys.models;

import java.io.Serializable;

public class orderPizzaItem implements Serializable
{

    private static final long serialVersionUID = -6009190778756332608L;

    public int getPizzaId() {
        return pizzaId;
    }

    public void setPizzaId(int pizzaId) {
        this.pizzaId = pizzaId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public orderPizzaItem(int pizzaId, int quantity) {

        this.pizzaId = pizzaId;
        this.quantity = quantity;
    }

    int pizzaId;

    int quantity;




    protected  orderPizzaItem(){



    }
}


