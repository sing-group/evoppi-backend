INSERT INTO `database_information` values(1, 'development', 3, 3, 3, 3, 3);

INSERT INTO `user` (`login`, `role`, `email`, `password`)
VALUES ('admin', 'ADMIN', 'admin@email.com', '25E4EE4E9229397B6B17776BFCEAF8E7');
INSERT INTO `user` (`login`, `role`, `email`, `password`)
VALUES ('researcher', 'RESEARCHER', 'researcher@email.com', '9ADF82A517DCDC0C2087598ABAC14916');

INSERT INTO `administrator` (`login`) VALUES ('admin');
INSERT INTO `researcher` (`login`) VALUES ('researcher');
