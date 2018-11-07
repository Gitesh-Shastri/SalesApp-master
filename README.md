(#1) SignInActivity:
  ->Enter email address: gitesh@test.com
  ->Enter password: shastri
  
 (#2) PlaceOrderActivity
  #Wait till areas, pharmacies and medicines list is loading...
  #Select Area (pharmacy list will be populated according to area selected)
  ->"Frazer Town" selected area 
  ->"Frazer medicals" selected pharmacy
  #Select medicines:
      "HHCAL TROL"  qty-3  cost-297
      "HHLINCTUS LS SUP"  qty-2 cost-198
      "Dermadew aloe"  qty-5 cost-495
  ->Click on the "Shop medicine icon" in app bar
  
  (#3) ConfirmOrderActivity
  #List of all the selected medicines displayed alongwith cost
  ->Click on the "CONFIRM ORDER" button
  #Waiting for the order to be placed......
  
  
 (#4) OrderConfirmedActivity
 #Display selected Pharmacy "Frazer medicals"
 #Display order Id
 #Display delivery date
 #Display total cost
