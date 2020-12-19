-- Generated by Oracle SQL Developer Data Modeler 19.4.0.350.1424
--   at:        2020-12-12 17:53:39 CET
--   site:      Oracle Database 12c
--   type:      Oracle Database 12c



CREATE TABLE autor (
    id_autora         INTEGER NOT NULL,
    imie              VARCHAR2(20) NOT NULL,
    nazwisko          VARCHAR2(30) NOT NULL,
    kraj_pochodzenia  VARCHAR2(30)
);

CREATE INDEX autor__idx_nazw ON
    autor (
        nazwisko
    ASC );

ALTER TABLE autor ADD CONSTRAINT autor_pk PRIMARY KEY ( id_autora );

CREATE TABLE autor_produktu (
    autor_id_autora      INTEGER NOT NULL,
    produkt_id_produktu  INTEGER NOT NULL
);

CREATE INDEX autor_produktu__idx_autor ON
    autor_produktu (
        autor_id_autora
    ASC );

ALTER TABLE autor_produktu ADD CONSTRAINT autor_produktu_pk PRIMARY KEY ( autor_id_autora,
                                                                          produkt_id_produktu );

CREATE TABLE element_koszyka (
    "L.p."                      INTEGER NOT NULL,
    produkt_id_produktu         INTEGER NOT NULL,
    koszyk_zakupowy_nr_koszyka  INTEGER NOT NULL,
    liczba                      INTEGER NOT NULL,
    wartosc_artykulu            NUMBER(7, 2) NOT NULL
);

ALTER TABLE element_koszyka ADD CONSTRAINT element_koszyka_pk PRIMARY KEY ( "L.p.",
                                                                            koszyk_zakupowy_nr_koszyka );

CREATE TABLE gra_planszowa (
    id_produktu   INTEGER NOT NULL,
    g_min_gracze  INTEGER NOT NULL,
    g_max_gracze  INTEGER NOT NULL,
    g_min_wiek    INTEGER,
    g_czas_gry    INTEGER
);

ALTER TABLE gra_planszowa ADD CONSTRAINT gra_planszowa_ck_1 CHECK ( g_min_gracze <= g_max_gracze );

ALTER TABLE gra_planszowa ADD CONSTRAINT gra_planszowa_pk PRIMARY KEY ( id_produktu );

CREATE TABLE klient (
    login           VARCHAR2(30) NOT NULL,
    k_adres_e_mail  VARCHAR2(30) NOT NULL,
    k_adres         VARCHAR2(50) NOT NULL
);

ALTER TABLE klient ADD CONSTRAINT klient_pk PRIMARY KEY ( login );

CREATE TABLE koszyk_zakupowy (
    klient_login                  VARCHAR2(30) NOT NULL,
    nr_koszyka                    INTEGER NOT NULL,
    wartosc_zakupow               NUMBER(8, 2) NOT NULL,
    sposob_platnosci              VARCHAR2(20) NOT NULL,
    koszt_wysylki                 NUMBER(4, 2) NOT NULL,
    calkowita_wartosc_zamowienia  NUMBER(8, 2) NOT NULL
);

CREATE INDEX koszyk_zakupowy__idx_klient ON
    koszyk_zakupowy (
        klient_login
    ASC );

ALTER TABLE koszyk_zakupowy ADD CONSTRAINT koszyk_zakupowy_pk PRIMARY KEY ( nr_koszyka );

CREATE TABLE ksiazka (
    id_produktu     INTEGER NOT NULL,
    k_typ_okladki   VARCHAR2(10),
    k_liczba_stron  INTEGER,
    k_format        VARCHAR2(4),
    seria_tytul     VARCHAR2(50)
);

ALTER TABLE ksiazka ADD CONSTRAINT ksiazka_pk PRIMARY KEY ( id_produktu );

CREATE TABLE pracownik (
    login                VARCHAR2(30) NOT NULL,
    p_data_zatrudnienia  DATE NOT NULL,
    p_pensja_brutto      NUMBER(7, 2) NOT NULL,
    p_stanowisko         VARCHAR2(20) NOT NULL,
    p_typ_umowy          VARCHAR2(30) NOT NULL
);

ALTER TABLE pracownik ADD CONSTRAINT pracownik_pk PRIMARY KEY ( login );

CREATE TABLE produkt (
    id_produktu                 INTEGER NOT NULL,
    nazwa                       VARCHAR2(30) NOT NULL,
    cena                        NUMBER(5, 2) NOT NULL,
    rok_wydania                 DATE,
    stan_magazyn                INTEGER NOT NULL,
    wydawnictwo_id_wydawnictwa  INTEGER NOT NULL,
    co                          VARCHAR2(1) NOT NULL
);

ALTER TABLE produkt
    ADD CONSTRAINT ch_inh_produkt CHECK ( co IN (
        'g',
        'k'
    ) );

CREATE INDEX produkt__idx ON
    produkt (
        nazwa
    ASC );

CREATE INDEX produkt__idx_cena ON
    produkt (
        cena
    ASC );

ALTER TABLE produkt ADD CONSTRAINT produkt_pk PRIMARY KEY ( id_produktu );

CREATE TABLE seria (
    tytul         VARCHAR2(50) NOT NULL,
    liczba_tomow  INTEGER
);

ALTER TABLE seria ADD CONSTRAINT seria_pk PRIMARY KEY ( tytul );

CREATE TABLE uzytkownik (
    login          VARCHAR2(30) NOT NULL,
    haslo          VARCHAR2(20) NOT NULL,
    imie           VARCHAR2(20) NOT NULL,
    nazwisko       VARCHAR2(30) NOT NULL,
    nr_kontaktowy  VARCHAR2(9),
    kto            VARCHAR2(1) NOT NULL
);

ALTER TABLE uzytkownik
    ADD CONSTRAINT ch_inh_użytkownik CHECK ( kto IN (
        'k',
        'p'
    ) );

CREATE INDEX uzytkownik__idx ON
    uzytkownik (
        login
    ASC );

ALTER TABLE uzytkownik ADD CONSTRAINT uzytkownik_pk PRIMARY KEY ( login );

CREATE TABLE wydawnictwo (
    id_wydawnictwa    INTEGER NOT NULL,
    nazwa             VARCHAR2(30) NOT NULL,
    kraj_pochodzenia  VARCHAR2(30)
);

ALTER TABLE wydawnictwo ADD CONSTRAINT wydawnictwo_pk PRIMARY KEY ( id_wydawnictwa );

ALTER TABLE autor_produktu
    ADD CONSTRAINT autor_produktu_autor_fk FOREIGN KEY ( autor_id_autora )
        REFERENCES autor ( id_autora );

ALTER TABLE autor_produktu
    ADD CONSTRAINT autor_produktu_produkt_fk FOREIGN KEY ( produkt_id_produktu )
        REFERENCES produkt ( id_produktu );

ALTER TABLE element_koszyka
    ADD CONSTRAINT el_kosz_kosz_zakup_fk FOREIGN KEY ( koszyk_zakupowy_nr_koszyka )
        REFERENCES koszyk_zakupowy ( nr_koszyka );

ALTER TABLE element_koszyka
    ADD CONSTRAINT element_koszyka_produkt_fk FOREIGN KEY ( produkt_id_produktu )
        REFERENCES produkt ( id_produktu );

ALTER TABLE gra_planszowa
    ADD CONSTRAINT gra_planszowa_produkt_fk FOREIGN KEY ( id_produktu )
        REFERENCES produkt ( id_produktu );

ALTER TABLE klient
    ADD CONSTRAINT klient_uzytkownik_fk FOREIGN KEY ( login )
        REFERENCES uzytkownik ( login )
            ON DELETE CASCADE;

ALTER TABLE koszyk_zakupowy
    ADD CONSTRAINT koszyk_zakupowy_klient_fk FOREIGN KEY ( klient_login )
        REFERENCES klient ( login );

ALTER TABLE ksiazka
    ADD CONSTRAINT ksiazka_produkt_fk FOREIGN KEY ( id_produktu )
        REFERENCES produkt ( id_produktu );

ALTER TABLE ksiazka
    ADD CONSTRAINT ksiazka_seria_fk FOREIGN KEY ( seria_tytul )
        REFERENCES seria ( tytul )
            ON DELETE CASCADE;

ALTER TABLE pracownik
    ADD CONSTRAINT pracownik_uzytkownik_fk FOREIGN KEY ( login )
        REFERENCES uzytkownik ( login )
            ON DELETE CASCADE;

ALTER TABLE produkt
    ADD CONSTRAINT produkt_wydawnictwo_fk FOREIGN KEY ( wydawnictwo_id_wydawnictwa )
        REFERENCES wydawnictwo ( id_wydawnictwa )
            ON DELETE CASCADE;

CREATE OR REPLACE TRIGGER arc_fkarc_2_pracownik BEFORE
    INSERT OR UPDATE OF login ON pracownik
    FOR EACH ROW
DECLARE
    d VARCHAR2(1);
BEGIN
    SELECT
        a.kto
    INTO d
    FROM
        uzytkownik a
    WHERE
        a.login = :new.login;

    IF ( d IS NULL OR d <> 'p' ) THEN
        raise_application_error(-20223, 'FK Pracownik_Uzytkownik_FK in Table Pracownik violates Arc constraint on Table Uzytkownik - discriminator column kto doesn''t have value ''p''');
    END IF;

EXCEPTION
    WHEN no_data_found THEN
        NULL;
    WHEN OTHERS THEN
        RAISE;
END;
/

CREATE OR REPLACE TRIGGER arc_fkarc_2_klient BEFORE
    INSERT OR UPDATE OF login ON klient
    FOR EACH ROW
DECLARE
    d VARCHAR2(1);
BEGIN
    SELECT
        a.kto
    INTO d
    FROM
        uzytkownik a
    WHERE
        a.login = :new.login;

    IF ( d IS NULL OR d <> 'k' ) THEN
        raise_application_error(-20223, 'FK Klient_Uzytkownik_FK in Table Klient violates Arc constraint on Table Uzytkownik - discriminator column kto doesn''t have value ''k''');
    END IF;

EXCEPTION
    WHEN no_data_found THEN
        NULL;
    WHEN OTHERS THEN
        RAISE;
END;
/

CREATE OR REPLACE TRIGGER arc_fkarc_4_gra_planszowa BEFORE
    INSERT OR UPDATE OF id_produktu ON gra_planszowa
    FOR EACH ROW
DECLARE
    d VARCHAR2(1);
BEGIN
    SELECT
        a.co
    INTO d
    FROM
        produkt a
    WHERE
        a.id_produktu = :new.id_produktu;

    IF ( d IS NULL OR d <> 'g' ) THEN
        raise_application_error(-20223, 'FK Gra_planszowa_Produkt_FK in Table Gra_planszowa violates Arc constraint on Table Produkt - discriminator column co doesn''t have value ''g''');
    END IF;

EXCEPTION
    WHEN no_data_found THEN
        NULL;
    WHEN OTHERS THEN
        RAISE;
END;
/

CREATE OR REPLACE TRIGGER arc_fkarc_4_ksiazka BEFORE
    INSERT OR UPDATE OF id_produktu ON ksiazka
    FOR EACH ROW
DECLARE
    d VARCHAR2(1);
BEGIN
    SELECT
        a.co
    INTO d
    FROM
        produkt a
    WHERE
        a.id_produktu = :new.id_produktu;

    IF ( d IS NULL OR d <> 'k' ) THEN
        raise_application_error(-20223, 'FK Ksiazka_Produkt_FK in Table Ksiazka violates Arc constraint on Table Produkt - discriminator column co doesn''t have value ''k''');
    END IF;

EXCEPTION
    WHEN no_data_found THEN
        NULL;
    WHEN OTHERS THEN
        RAISE;
END;
/

CREATE SEQUENCE autor_id_autora_seq START WITH 1 NOCACHE ORDER;

CREATE OR REPLACE TRIGGER autor_id_autora_trg BEFORE
    INSERT ON autor
    FOR EACH ROW
    WHEN ( new.id_autora IS NULL )
BEGIN
    :new.id_autora := autor_id_autora_seq.nextval;
END;
/

CREATE SEQUENCE koszyk_zakupowy_nr_koszyka_seq START WITH 1 NOCACHE ORDER;

CREATE OR REPLACE TRIGGER koszyk_zakupowy_nr_koszyka_trg BEFORE
    INSERT ON koszyk_zakupowy
    FOR EACH ROW
    WHEN ( new.nr_koszyka IS NULL )
BEGIN
    :new.nr_koszyka := koszyk_zakupowy_nr_koszyka_seq.nextval;
END;
/

CREATE SEQUENCE produkt_id_produktu_seq START WITH 1 NOCACHE ORDER;

CREATE OR REPLACE TRIGGER produkt_id_produktu_trg BEFORE
    INSERT ON produkt
    FOR EACH ROW
    WHEN ( new.id_produktu IS NULL )
BEGIN
    :new.id_produktu := produkt_id_produktu_seq.nextval;
END;
/

CREATE SEQUENCE wydawnictwo_id_wydawnictwa_seq START WITH 1 NOCACHE ORDER;

CREATE OR REPLACE TRIGGER wydawnictwo_id_wydawnictwa_trg BEFORE
    INSERT ON wydawnictwo
    FOR EACH ROW
    WHEN ( new.id_wydawnictwa IS NULL )
BEGIN
    :new.id_wydawnictwa := wydawnictwo_id_wydawnictwa_seq.nextval;
END;

create or replace PROCEDURE nowaKsiazka
    (vNazwa IN VARCHAR2,
    vCena IN NUMERIC,
    vRok_wydania IN DATE,
    vStan IN NUMBER,
    vWydawnictwo IN VARCHAR2,
    vTyp_okladki IN VARCHAR2,
    vLiczba_stron IN NUMBER,
    vFormat IN VARCHAR2,
    vSeria IN VARCHAR2) IS
BEGIN
    INSERT INTO Produkt VALUES(PRODUKT_ID_PRODUKTU_SEQ.nextval, vNazwa, vCena, vRok_wydania, vStan, vWydawnictwo, 'k');
    INSERT INTO Ksiazka VALUES(PRODUKT_ID_PRODUKTU_SEQ.nextval, vTyp_okladki, vLiczba_stron, vFormat, vSeria);
END nowaKsiazka;
create or replace PROCEDURE nowaGra
    (vNazwa IN VARCHAR2,
    vCena IN NUMERIC,
    vRok_wydania IN DATE,
    vStan IN NUMBER,
    vWydawnictwo IN VARCHAR2,
    vMin_gracze IN NUMBER,
    vMax_gracze IN NUMBER,
    vMin_wiek IN NUMBER,
    vCzas_gry IN NUMBER) IS
BEGIN
    INSERT INTO Produkt VALUES(PRODUKT_ID_PRODUKTU_SEQ.nextval, vNazwa, vCena, vRok_wydania, vStan, vWydawnictwo, 'g');
    INSERT INTO gra_planszowa VALUES(PRODUKT_ID_PRODUKTU_SEQ.nextval, vMin_gracze, vMax_gracze, vMin_wiek, vCzas_gry);
END nowaGra;
create or replace PROCEDURE nowyPracownik
    (vLogin IN VARCHAR2,
    vHaslo IN VARCHAR2,
    vImie IN VARCHAR2,
    vNazwisko IN VARCHAR2,
    vNr IN VARCHAR2,
    vData_zatrudnienia IN DATE,
    vPensja IN FLOAT,
    vStanowisko IN VARCHAR2,
    vTyp_umowy IN VARCHAR2) IS
BEGIN
    INSERT INTO Uzytkownik VALUES(vLogin, vHaslo, vImie, vNazwisko, vNr, 'p');
    INSERT INTO Pracownik VALUES(vLogin, vData_zatrudnienia, vPensja, vStanowisko, vTyp_umowy);
END nowyPracownik;
create or replace PROCEDURE nowyKlient
    (vLogin IN VARCHAR2,
    vHaslo IN VARCHAR2,
    vImie IN VARCHAR2,
    vNazwisko IN VARCHAR2,
    vNr IN VARCHAR2,
    vE_mail IN VARCHAR2,
    vAdres IN VARCHAR2) IS
BEGIN
    INSERT INTO Uzytkownik VALUES(vLogin, vHaslo, vImie, vNazwisko, vNr, 'k');
    INSERT INTO Klient VALUES(vLogin, vE_mail, vAdres);
END nowyKlient;
create or replace FUNCTION checkPwd 
    (login_to_check IN VARCHAR2, 
     pass_to_check IN VARCHAR2)
    RETURN NUMBER IS 
    res NUMBER;
    user_log VARCHAR2(30);
    user_pass VARCHAR2(30);
BEGIN 
    SELECT login, Haslo INTO user_log, user_pass FROM Uzytkownik WHERE login = login_to_check;
    IF user_pass = pass_to_check THEN
        res := 1;
    ELSE res := 0;
    END IF;
    RETURN res;
END checkPwd;



-- Oracle SQL Developer Data Modeler Summary Report: 
-- 
-- CREATE TABLE                            12
-- CREATE INDEX                             6
-- ALTER TABLE                             26
-- CREATE VIEW                              0
-- ALTER VIEW                               0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         4 
-- CREATE FUNCTION                          1
-- CREATE TRIGGER                           8
-- ALTER TRIGGER                            0
-- CREATE COLLECTION TYPE                   0
-- CREATE STRUCTURED TYPE                   0
-- CREATE STRUCTURED TYPE BODY              0
-- CREATE CLUSTER                           0
-- CREATE CONTEXT                           0
-- CREATE DATABASE                          0
-- CREATE DIMENSION                         0
-- CREATE DIRECTORY                         0
-- CREATE DISK GROUP                        0
-- CREATE ROLE                              0
-- CREATE ROLLBACK SEGMENT                  0
-- CREATE SEQUENCE                          4
-- CREATE MATERIALIZED VIEW                 0
-- CREATE MATERIALIZED VIEW LOG             0
-- CREATE SYNONYM                           0
-- CREATE TABLESPACE                        0
-- CREATE USER                              0
-- 
-- DROP TABLESPACE                          0
-- DROP DATABASE                            0
-- 
-- REDACTION POLICY                         0
-- TSDP POLICY                              0
-- 
-- ORDS DROP SCHEMA                         0
-- ORDS ENABLE SCHEMA                       0
-- ORDS ENABLE OBJECT                       0
-- 
-- ERRORS                                   0
-- WARNINGS                                 0
