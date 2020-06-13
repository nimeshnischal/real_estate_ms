CREATE TABLE IF NOT EXISTS `property`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `price` double NOT NULL,
  `bedroom_count` double NOT NULL,
  `bathroom_count` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `requirement` (
  `id` bigint(20) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `min_budget` double NOT NULL,
  `max_budget` double NOT NULL,
  `mean_budget` double NOT NULL,
  `range_budget` double NOT NULL,
  `min_bedrooms` double NOT NULL,
  `max_bedrooms` double NOT NULL,
  `min_bathrooms` int(11) NOT NULL,
  `max_bathrooms` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
