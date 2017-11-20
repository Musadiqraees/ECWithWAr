package de.tub.ise.anwsys.controllers;

import de.tub.ise.anwsys.models.OrderPizza;
import de.tub.ise.anwsys.models.Pizza;

import de.tub.ise.anwsys.models.Topping;
import de.tub.ise.anwsys.models.Pizza.Size;
import de.tub.ise.anwsys.models.orderPizzaItem;
import de.tub.ise.anwsys.repos.OrderRepository;
import de.tub.ise.anwsys.repos.PizzaRepository;
import de.tub.ise.anwsys.repos.ToppingRepository;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class PizzaController {


	private PizzaRepository PizzaRepository;

	private ToppingRepository ToppingRepository;



	private OrderRepository orderRepository;


	@Autowired
	public PizzaController(PizzaRepository PizzaRepository, ToppingRepository ToppingRepository, OrderRepository orderRepository) {
		this.PizzaRepository = PizzaRepository;
		this.ToppingRepository = ToppingRepository;

		this.orderRepository = orderRepository;
	}


	public PizzaController() {
		// TODO Auto-generated constructor stub
	}


	////////Added new pizza

	@RequestMapping(method = RequestMethod.POST, path = "/pizza", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> addPizza(@RequestBody Pizza pizza, UriComponentsBuilder b) {

//		 if(PizzaRepository.exists(pizza.getId())) 
//		 {
//		
//			 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Invalid input")); 
//	 }
//		 else {

		if (pizza.getSize().equals(null) || pizza.getName().equals(null)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Invalid input"));
		} else {

			Pizza p = new Pizza(pizza.getName(), pizza.getSize());

			PizzaRepository.save(p);

			HttpHeaders headers = new HttpHeaders();

			UriComponents uriComponents =
					b.path("/pizza/{id}").buildAndExpand(pizza.getId());

			headers.setLocation(uriComponents.toUri());

			return new ResponseEntity<>("Created new pizza", headers, HttpStatus.CREATED);

			//return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Created new pizza.")).created(location);

			// }
		}
	}
	///update

	@RequestMapping(method = RequestMethod.PUT, path = "/pizza/{pizzaId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> updatePizza(@RequestBody Pizza pizza, @PathVariable("pizzaId") Integer PizzaId) {

		if (PizzaRepository.findOne(PizzaId) != null) {
			//pizza.setId(PizzaId);

			Pizza oldPizza = PizzaRepository.findOne(PizzaId);

			if (pizza.getName() != null) {
				oldPizza.setName(pizza.getName());
			}

			if (pizza.getPrice() != null) {
				if (pizza.getSize().equals(Size.Standard)) {
					oldPizza.setPrice((float) 5.0);
				} else if (pizza.getSize().equals(Size.Large)) {

					oldPizza.setPrice((float) 8.5);
				} else {
					oldPizza.setPrice(pizza.getPrice());
				}
			}

			if (pizza.getSize() != null) {
				oldPizza.setSize(pizza.getSize());
			}

			if (pizza.getToppingIds() != null) {
				oldPizza.setToppingIds(pizza.getToppingIds());
			}

			PizzaRepository.save(oldPizza);

			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(String.format("Update okay"));

		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Pizza not found"));
		}
	}


////////DElete pizza with PizzaId


	@RequestMapping(method = RequestMethod.DELETE, path = "/pizza/{pizzaId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> removePizzaWithID(@PathVariable("pizzaId") Integer PizzaId) {

		if (PizzaRepository.findOne(PizzaId) != null) {
			PizzaRepository.delete(PizzaId);

			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("deleted");

		} else {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Pizza not found"));
		}
	}


////////get pizza with PizzaId


	@RequestMapping(method = RequestMethod.GET, path = "/pizza/{pizzaId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> getPizzaWithID(@PathVariable("pizzaId") Integer PizzaId) {

		if (PizzaRepository.findOne(PizzaId) != null) {


			Pizza pizza = PizzaRepository.findOne(PizzaId);

			pizza = calPizzaPrice(pizza);


			return ResponseEntity.ok(pizza);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Pizza could not be found"));
		}

	}


////////get all pizza


	@RequestMapping(method = RequestMethod.GET, path = "/pizza", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> getAllpizza() {
		ArrayList<Pizza> PizzaObjects = new ArrayList<Pizza>();

		PizzaObjects = (ArrayList<Pizza>) PizzaRepository.findAll();

		ArrayList<Pizza> newPizzaObjects = new ArrayList<Pizza>();

		for (int i = 0; i < PizzaObjects.size(); i++) {
			Pizza pizza = PizzaObjects.get(i);

			pizza = calPizzaPrice(pizza);

			newPizzaObjects.add(pizza);

		}


		if (newPizzaObjects.isEmpty()) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("No pizzas exist"));

		} else {

			return ResponseEntity.ok(newPizzaObjects);
		}
	}


//	 @RequestMapping(method = RequestMethod.POST, path = "/AddPizza")
//	    public ResponseEntity<?> AddPizza(@RequestParam(value = "name", defaultValue = "AnwSys Student") String name, @RequestParam (value = "Id", defaultValue = "23" ) Integer Id
//	            , @RequestParam (value = "size", defaultValue = "12" ) Size size
//	            , @RequestParam(value = "price", defaultValue = "23" ) Integer price) {
//	    	
//		 
//		 if(PizzaRepository.findOne(Id) != null) 
//		 {
//			
//			 return ResponseEntity.ok(String.format("pizza with this ID  %s! already exist", Id ));  
//		 }
//		 else
//		 {
//		 
//		 Pizza tempPizza = new Pizza( name, Id , price,  size ) ;
//		 
//		   PizzaRepository.save(tempPizza);
//	    	
//	    	return ResponseEntity.status(HttpStatus.NO_CONTENT).body(String.format("Pizza Added check detail %s!", name+price+size)); 
//	    
//		  }
//		  
//	    	
//	    }
//	/////////////////////////////////////////
	////////////////////////////////Topping////////
	//////////////////////////////////////////////////////////////////

	//addingPizzaTopping
	@RequestMapping(method = RequestMethod.POST, path = "/pizza/{pizzaId}/topping", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> addPizzaTopping(@RequestBody Topping topping, @PathVariable("pizzaId") Integer pizzaId, UriComponentsBuilder b) {

		if (PizzaRepository.exists(pizzaId)) {
			//topping.setPizzaId(pizzaId);

			Pizza pizza = PizzaRepository.findOne(pizzaId);

			ArrayList<Integer> ToppingIds = pizza.getToppingIds();

			topping = ToppingRepository.save(topping);

			ToppingIds.add(topping.getId());

			pizza.setToppingIds(ToppingIds);

//			Float toppingCost = topping.getPrice();
//
//			Float pizzaCost =  pizza.getPrice();
//
//			Float totalPizzaPrice=  toppingCost+pizzaCost;
//
//			pizza.setPrice(totalPizzaPrice);

			PizzaRepository.save(pizza);


			HttpHeaders headers = new HttpHeaders();

			UriComponents uriComponents =
					b.path("/pizza/{pizzaId}/topping/{toppingId}").buildAndExpand(pizza.getId(), topping.getId());

			headers.setLocation(uriComponents.toUri());

			return new ResponseEntity<>("Created new Topping for pizza.", headers, HttpStatus.CREATED);


			//return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Created new Topping for pizza."));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Invalid input"));

		}
	}


	//getPizzaToppings
	@RequestMapping(method = RequestMethod.GET, path = "/pizza/{pizzaId}/topping", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> getPizzaToppings(@PathVariable("pizzaId") Integer pizzaId) {

		if (PizzaRepository.exists(pizzaId)) {
			Pizza pizza = PizzaRepository.findOne(pizzaId);

			ArrayList<Integer> PizzaObjects = new ArrayList<Integer>();

			PizzaObjects = pizza.getToppingIds();

			return ResponseEntity.status(HttpStatus.CREATED).body(PizzaObjects);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Invalid input"));

		}
	}


	//getListOfPizzaToppingsBYID
	@RequestMapping(method = RequestMethod.GET, path = "/pizza/{pizzaId}/topping/{toppingId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> getPizzaToppingById(@PathVariable("pizzaId") Integer pizzaId, @PathVariable("toppingId") Integer toppingId) {

		if (PizzaRepository.exists(pizzaId)) {
			Pizza pizza = PizzaRepository.findOne(pizzaId);

			ArrayList<Integer> toppingObjects = new ArrayList<Integer>();

			toppingObjects = pizza.getToppingIds();

			if (toppingObjects.contains(toppingId)) {

				Topping toppingObj = ToppingRepository.findOne(toppingId);

				return ResponseEntity.status(HttpStatus.CREATED).body(toppingObj);
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Invalid ID(s) supplied."));

		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Pizza or Topping not found."));

		}
	}


	//deletePizzaToppingsBYID
	@RequestMapping(method = RequestMethod.DELETE, path = "/pizza/{pizzaId}/topping/{toppingId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> deletePizzaToppingById(@PathVariable("pizzaId") Integer pizzaId, @PathVariable("toppingId") Integer toppingId) {

		if (PizzaRepository.exists(pizzaId)) {
			Pizza pizza = PizzaRepository.findOne(pizzaId);

			ArrayList<Integer> toppingObjects = new ArrayList<Integer>();

			toppingObjects = pizza.getToppingIds();

			if (toppingObjects.contains(toppingId)) {

				Topping toppingObj = ToppingRepository.findOne(toppingId);


				////////price change in pizza after topping is removed
//					Float pizzaPrice =  pizza.getPrice();
//
//					Float toppingPrice =  toppingObj.getPrice();
//
//				 Float newPizzaPrice = pizzaPrice - toppingPrice;
//
//				 pizza.setPrice(newPizzaPrice);


				//////// remove topping Id

				toppingObjects.remove(toppingId);

				pizza.setToppingIds(toppingObjects);

				PizzaRepository.save(pizza);


				/////removing topping

				ToppingRepository.delete(toppingId);

				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(toppingObj);
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Invalid ID(s) supplied."));

		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Pizza or Topping not found."));

		}
	}


	///////////////////
	/////////////////////////////
	/////////OrderItem


	@RequestMapping(method = RequestMethod.POST, path = "/order", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> addPizzaOrder(@RequestBody OrderPizza order, UriComponentsBuilder b) {

//				 if(orderRepository.exists(order.getId())) 
//				 {
//				
//				 return ResponseEntity.ok(String.format("Invalid Order. "));  
//			 }
//				 else {

		 ArrayList<orderPizzaItem> arrayOfItems = order.getOrderItems();

//
				for (int i = 0; i < arrayOfItems.size(); i++)
				{
					orderPizzaItem itemObj= arrayOfItems.get(i);

					if(PizzaRepository.exists(itemObj.getPizzaId()))
					{

					}
					else{

						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("order not valid."));

					}

					}

		orderRepository.save(order);

//					 if(arrayOfItems.size() > 0)
//					 {
//					 OrderItem itemObj= arrayOfItems.get(0);
//
//					 if(PizzaRepository.exists(itemObj.getPizzaId()))
//					 {

		orderRepository.save(order);

		HttpHeaders headers = new HttpHeaders();

		UriComponents uriComponents =
				b.path("/pizza/{orderId}").buildAndExpand(order.getId());

		headers.setLocation(uriComponents.toUri());

		return new ResponseEntity<>("Created new order successfully.", headers, HttpStatus.CREATED);

		  //return ResponseEntity.status(HttpStatus.CREATED).body(String.format("sdfs"));

		// }

		//return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Pizza Id not valid."));

	}
					// return ResponseEntity.status(HttpStatus.BAD_REQUEST).

	//body(String.format("send proper body structure"));
//
	// }
	//}
//}
			
			/////////getting All pizza order
			@RequestMapping(method = RequestMethod.GET, path = "/order" ,consumes = MediaType.APPLICATION_JSON_VALUE, produces = { MediaType.APPLICATION_JSON_VALUE})
		    public ResponseEntity<?> getAllPizzaOrder( ) 
			{
				 
				 if(orderRepository.count()==0) 
				 {
				
				 return ResponseEntity.ok(String.format("Invalid Order. "));  
			 }
				 else {

					 ArrayList<OrderPizza> arrayOfItems =(ArrayList) orderRepository.findAll();

					 ArrayList <Integer> idsArray= new ArrayList<Integer>();

					 for (int i = 0; i <arrayOfItems.size() ; i++)
					 {
						OrderPizza orderPizza= arrayOfItems.get(i);
						 idsArray.add(orderPizza.getId()) ;

					 }



					 return ResponseEntity.status(HttpStatus.CREATED).body(idsArray);

			} 
		 
			}
			
			
	/////////getting  pizza order by id
				@RequestMapping(method = RequestMethod.GET, path = "/order/{orderid}" ,consumes = MediaType.APPLICATION_JSON_VALUE, produces = { MediaType.APPLICATION_JSON_VALUE})
			    public ResponseEntity<?> getPizzaOrder( @PathVariable ("orderid") Integer orderid) 
				{
					 
					if(orderRepository.exists(orderid)) 
					 {
					 	float totalPrice=0;

                           OrderPizza order = orderRepository.findOne(orderid);

                         ArrayList orderItemsArray = order.getOrderItems();

						 for (int i = 0; i < orderItemsArray.size(); i++)
						 {
							 orderPizzaItem orderItem= (orderPizzaItem) orderItemsArray.get(i);

							Integer pizzaId= orderItem.getPizzaId();

							Pizza pizza =  PizzaRepository.findOne(pizzaId);

							pizza = calPizzaPrice(pizza);

							Integer quantity= orderItem.getQuantity();

							totalPrice+=pizza.getPrice()*quantity;

						 }

						 order.setTotalPrice(totalPrice);

						 return ResponseEntity.status(HttpStatus.CREATED).body(order); 
				      }
					 else 
					 {
						 return ResponseEntity.ok(String.format("Invalid Order. "));  
						 
				} 
			 
				}
				
				
				
				//delete  pizza by id
				@RequestMapping(method = RequestMethod.DELETE, path = "/order/{orderid}" ,consumes = MediaType.APPLICATION_JSON_VALUE, produces = { MediaType.APPLICATION_JSON_VALUE})
			    public ResponseEntity<?> deletePizzaOrder( @PathVariable  ("orderid") Integer orderid) 
				{
					 
					if(orderRepository.exists(orderid)) 
					 {
                          orderRepository.delete(orderid);
						 
						 return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deletion successfull"); 
				      }
					 else 
					 {
						 return ResponseEntity.ok(String.format("Invalid Order. "));  
						 
				} 
			 
				}

	public Pizza calPizzaPrice(Pizza pizza)
	  {



		Float pizzaCost=pizza.getPrice();

		ArrayList pizzaToppingIds= pizza.getToppingIds();

		for (int i = 0; i < pizzaToppingIds.size() ; i++)
		{

			Integer toppingID = (Integer) pizzaToppingIds.get(i);
			Topping topping = ToppingRepository.findOne(toppingID );
			pizzaCost+= topping.getPrice();

		}

		pizza.setPrice(pizzaCost);



		return pizza;
	}
				
	 
}
