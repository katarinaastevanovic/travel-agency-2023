DROP SCHEMA IF EXISTS agency;
CREATE SCHEMA agency DEFAULT CHARACTER SET utf8;
USE agency;

CREATE TABLE users (
	id BIGINT AUTO_INCREMENT,
    username VARCHAR(20) NOT NULL,
    sifra VARCHAR(20) NOT NULL,
	ime VARCHAR(20) NOT NULL,
	lastName VARCHAR(20) NOT NULL,
    email VARCHAR(50) NOT NULL,
    dateOfBirth date NOT NULL,
    address VARCHAR(40) NOT NULL,
    brojTelefona VARCHAR(20) NOT NULL,
    dateOfReg datetime NOT NULL,
    uloga VARCHAR(20) NOT NULL,
    isBlocked BOOLEAN NOT NULL,
	PRIMARY KEY(id)
);

drop table users;
select * from users where username=ana
UPDATE users SET isBlocked = 1 WHERE id = 3


CREATE TABLE destinacije (
	id BIGINT AUTO_INCREMENT,
	grad VARCHAR(75) NOT NULL,
	drzava VARCHAR(75) NOT NULL,
	kontinent VARCHAR(75) NOT NULL,
    slika VARCHAR(255)
	PRIMARY KEY(id)
);
select * from vehicles

CREATE TABLE travels (
	id BIGINT AUTO_INCREMENT,
    destinacijaId BIGINT NOT NULL,
     vehicleId BIGINT NOT NULL,
     accUnitId BIGINT NOT NULL,
     typeOfTravel VARCHAR(50) NOT NULL,
     arrivalDate DATETIME NOT NULL,
     dateOfDeparture DATETIME NOT NULL,
     numberOfNights INT NOT NULL,
     price DOUBLE NOT NULL,
	PRIMARY KEY(id),
    FOREIGN KEY(destinacijaId) REFERENCES destinacije(id),
    FOREIGN KEY(vehicleId) REFERENCES vehicles(id),
	FOREIGN KEY(accUnitId) REFERENCES accUnits(id)
);
INSERT INTO travels ( destinacijaId, vehicleId,accUnitId,typeOfTravel,arrivalDate,dateOfDeparture,numberOfNights,price) VALUES (1, 5, 1,'WINTER_VACATION','2024-04-04','2024-04-05',1,400.0);
INSERT INTO travels ( destinacijaId, vehicleId,accUnitId,typeOfTravel,arrivalDate,dateOfDeparture,numberOfNights,price)VALUES (2, 4, 2,'NEW_YEARS','2024-01-01','2024-01-01',0,999.0);
INSERT INTO travels ( destinacijaId, vehicleId,accUnitId,typeOfTravel,arrivalDate,dateOfDeparture,numberOfNights,price)VALUES (3, 3, 3,'LAST_MINUTE','2024-07-23','2024-07-26',3,700.0);
select *from accUnits
select *from destinacije
select *from vehicles
select * from travels
drop table travels


CREATE TABLE vehicles (
	id BIGINT AUTO_INCREMENT,
    typeOfVehicle ENUM('AIRPLANE', 'BUS','SHIP') NOT NULL,
    numberOfSeats INT NOT NULL,
     finalDestinationID  BIGINT NOT NULL,
     descriptions varchar(200) NOT NULL,
     PRIMARY KEY(id),
     FOREIGN KEY(finalDestinationID) REFERENCES destinacije(id)
);

INSERT INTO vehicles (typeOfVehicle, numberOfSeats, finalDestinationID,descriptions) VALUES ('AIRPLANE', 1000, 1,'Travelling airplane');
INSERT INTO vehicles (typeOfVehicle, numberOfSeats, finalDestinationID,descriptions)VALUES ('SHIP', 500, 2,'Business class ship');
INSERT INTO vehicles (typeOfVehicle, numberOfSeats, finalDestinationID,descriptions) VALUES ('BUS', 100, 3,'Regular bus');
select * from vehicles

CREATE TABLE accUnits (
	id BIGINT AUTO_INCREMENT,
    namee  varchar(100) NOT NULL,
    typeOfAccommodationUnit VARCHAR(50) NOT NULL,
    capacity INT NOT NULL,
     destinacijaId BIGINT NOT NULL,
     reviews  varchar(200) NOT NULL,
     services  varchar(100) NOT NULL,
     descriptions varchar(100) NOT NULL,
     PRIMARY KEY(id),
	FOREIGN KEY(destinacijaId) REFERENCES destinacije(id)
);
INSERT INTO accUnits (namee, typeOfAccommodationUnit, capacity,destinacijaId,reviews,services,descriptions) VALUES ('Apartmans Taiyo','APARTMENT', 50, 1,'10/10','wi-fi','Very nice view');
INSERT INTO accUnits (namee, typeOfAccommodationUnit, capacity,destinacijaId,reviews,services,descriptions)VALUES ('Best Italian Hotel','HOTEL_OVERNIGHT_BREAKFAST', 1000, 2,'10/10','tv','Pretty luxurious');
INSERT INTO accUnits (namee, typeOfAccommodationUnit, capacity,destinacijaId,reviews,services,descriptions) VALUES ('Big Ben Hotel', 'HOTEL_OVERNIGHT', 450, 3,'8/10','tv','Has a lot of space');
select * from accUnits
delete * from accUnits
drop table accUnits
select * from vehicle
select * from travels
select * from users
UPDATE accUnits
SET typeOfAccommodationUnit = 'ADMIN'
WHERE username = 'kaca';


INSERT INTO destinacije (grad, drzava, kontinent, slika) VALUES ('Tokio', 'Japan', 'Azija','slika2jpg');
INSERT INTO destinacije (grad, drzava, kontinent, slika) VALUES ('Milano', 'Italija', 'Evropa','slika2jpg');
INSERT INTO destinacije (grad, drzava, kontinent, slika) VALUES ('London', 'Engleska', 'Evropa','slika2jpg');

select * from destinacije
select * from travels


select k.id,k.naziv,k.registarskiBrojPrimerka, k.jezik, k.brojStranica, k.izdata, a.id, a.ime, a.prezime from knjige k
 left join autoriKnjige on k.id = knjigaId
 left join autori a on autorId = a.id
 
 select d.grad, d.drzava,d.kontinent,d.picture from destinacije d
 where d.id=?
 order by d.id
 
 left join autoriKnjige on k.id = knjigaId
 left join autori a on autorId = a.id
 
 select k.id,k.naziv,k.registarskiBrojPrimerka, k.jezik, k.brojStranica, k.izdata from knjige k
							 left join autoriKnjige on k.id = knjigaId
						 left join autori a on autorId = a.id;
				
				SELECT k.id, k.naziv, k.registarskiBrojPrimerka, k.jezik, k.brojStranica, k.izdata FROM knjige k 
				WHERE k.id = ?  
				ORDER BY k.id