-- EvoPPI v0.1.0

INSERT INTO `user` (`login`, `role`, `email`, `password`)
VALUES ('admin', 'ADMIN', 'admin@email.com', '25E4EE4E9229397B6B17776BFCEAF8E7');
INSERT INTO `user` (`login`, `role`, `email`, `password`)
VALUES ('researcher', 'RESEARCHER', 'researcher@email.com', '9ADF82A517DCDC0C2087598ABAC14916');

INSERT INTO `administrator` (`login`) VALUES ('admin');
INSERT INTO `researcher` (`login`) VALUES ('researcher');

INSERT INTO `gene` (`id`, `sequence`) VALUES ('59', 'MCEEEDSTALVCDNGSGLCKAGFAGDDAPRAVFPSIVGRPRHQGVMVGMGQKDSYVGDEAQSKRGILTLKYPIEHGIITNWDDMEKIWHHSFYNELRVAPEEHPTLLTEAPLNPKANREKMTQIMFETFNVPAMYVAIQAVLSLYASGRTTGIVLDSGDGVTHNVPIYEGYALPHAIMRLDLAGRDLTDYLMKILTERGYSFVTTAEREIVRDIKEKLCYVALDFENEMATAASSSSLEKSYELPDGQVITIGNERFRCPETLFQPSFIGMESAGIHETTYNSIMKCDIDIRKDLYANNVLSGGTTMYPGIADRMQKEITALAPSTMKIKIIAPPERKYSVWIGGSILASLSTFQQMWISKQEYDEAGPSIVHRKCF');
INSERT INTO `gene` (`id`, `sequence`) VALUES ('58', 'MCDEDETTALVCDNGSGLVKAGFAGDDAPRAVFPSIVGRPRHQGVMVGMGQKDSYVGDEAQSKRGILTLKYPIEHGIITNWDDMEKIWHHTFYNELRVAPEEHPTLLTEAPLNPKANREKMTQIMFETFNVPAMYVAIQAVLSLYASGRTTGIVLDSGDGVTHNVPIYEGYALPHAIMRLDLAGRDLTDYLMKILTERGYSFVTTAEREIVRDIKEKLCYVALDFENEMATAASSSSLEKSYELPDGQVITIGNERFRCPETLFQPSFIGMESAGIHETTYNSIMKCDIDIRKDLYANNVMSGGTTMYPGIADRMQKEITALAPSTMKIKIIAPPERKYSVWIGGSILASLSTFQQMWITKQEYDEAGPSIVHRKCF');
INSERT INTO `gene` (`id`, `sequence`) VALUES ('60', 'MDDDIAALVVDNGSGMCKAGFAGDDAPRAVFPSIVGRPRHQGVMVGMGQKDSYVGDEAQSKRGILTLKYPIEHGIVTNWDDMEKIWHHTFYNELRVAPEEHPVLLTEAPLNPKANREKMTQIMFETFNTPAMYVAIQAVLSLYASGRTTGIVMDSGDGVTHTVPIYEGYALPHAILRLDLAGRDLTDYLMKILTERGYSFTTTAEREIVRDIKEKLCYVALDFEQEMATAASSSSLEKSYELPDGQVITIGNERFRCPEALFQPSFLGMESCGIHETTFNSIMKCDVDIRKDLYANTVLSGGTTMYPGIADRMQKEITALAPSTMKIKIIAPPERKYSVWIGGSILASLSTFQQMWISKQEYDESGPSIVHRKCF');
INSERT INTO `gene` (`id`, `sequence`) VALUES ('71', 'MEEEIAALVIDNGSGMCKAGFAGDDAPRAVFPSIVGRPRHQGVMVGMGQKDSYVGDEAQSKRGILTLKYPIEHGIVTNWDDMEKIWHHTFYNELRVAPEEHPVLLTEAPLNPKANREKMTQIMFETFNTPAMYVAIQAVLSLYASGRTTGIVMDSGDGVTHTVPIYEGYALPHAILRLDLAGRDLTDYLMKILTERGYSFTTTAEREIVRDIKEKLCYVALDFEQEMATAASSSSLEKSYELPDGQVITIGNERFRCPEALFQPSFLGMESCGIHETTFNSIMKCDVDIRKDLYANTVLSGGTTMYPGIADRMQKEITALAPSTMKIKIIAPPERKYSVWIGGSILASLSTFQQMWISKQEYDESGPSIVHRKCF');

INSERT INTO `specie` (`name`) VALUES ('Homo Sapiens');

INSERT INTO `interactome` (`name`, `specieId`) VALUES ('HomoMINT', '1');
INSERT INTO `interactome` (`name`, `specieId`) VALUES ('Interactome3D', '1');

INSERT INTO `interaction` (`interactome`, `geneFrom`, `geneTo`) VALUES ('1', '58', '58');
INSERT INTO `interaction` (`interactome`, `geneFrom`, `geneTo`) VALUES ('1', '59', '59');
INSERT INTO `interaction` (`interactome`, `geneFrom`, `geneTo`) VALUES ('1', '58', '59');
INSERT INTO `interaction` (`interactome`, `geneFrom`, `geneTo`) VALUES ('1', '59', '58');
INSERT INTO `interaction` (`interactome`, `geneFrom`, `geneTo`) VALUES ('1', '58', '60');
INSERT INTO `interaction` (`interactome`, `geneFrom`, `geneTo`) VALUES ('1', '59', '60');
INSERT INTO `interaction` (`interactome`, `geneFrom`, `geneTo`) VALUES ('1', '60', '58');
INSERT INTO `interaction` (`interactome`, `geneFrom`, `geneTo`) VALUES ('1', '60', '59');
INSERT INTO `interaction` (`interactome`, `geneFrom`, `geneTo`) VALUES ('2', '60', '60');
INSERT INTO `interaction` (`interactome`, `geneFrom`, `geneTo`) VALUES ('2', '60', '71');