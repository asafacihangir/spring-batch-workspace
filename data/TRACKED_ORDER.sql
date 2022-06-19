CREATE TABLE `TRACKED_ORDER` (
  `order_id` int(11) DEFAULT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `cost` varchar(50) DEFAULT NULL,
  `item_id` varchar(40) DEFAULT NULL,
  `item_name` varchar(15) DEFAULT NULL,
  `ship_date` date DEFAULT NULL,
  `tracking_number` varchar(100) DEFAULT NULL,
  `free_shipping` boolean DEFAULT NULL
);

