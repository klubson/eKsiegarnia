-- Generated by Oracle SQL Developer Data Modeler 19.2.0.182.1216
--   at:        2020-11-21 15:09:13 CET
--   site:      Oracle Database 12c
--   type:      Oracle Database 12c



DROP TABLE autor CASCADE CONSTRAINTS;

DROP TABLE autor_produktu CASCADE CONSTRAINTS;

DROP TABLE element_koszyka CASCADE CONSTRAINTS;

DROP TABLE gra_planszowa CASCADE CONSTRAINTS;

DROP TABLE klient CASCADE CONSTRAINTS;

DROP TABLE koszyk_zakupowy CASCADE CONSTRAINTS;

DROP TABLE ksiazka CASCADE CONSTRAINTS;

DROP TABLE pracownik CASCADE CONSTRAINTS;

DROP TABLE produkt CASCADE CONSTRAINTS;

DROP TABLE seria CASCADE CONSTRAINTS;

DROP TABLE uzytkownik CASCADE CONSTRAINTS;

DROP TABLE wydawnictwo CASCADE CONSTRAINTS;

CREATE TABLE autor (
    id_autora          INTEGER NOT NULL,
    imie               VARCHAR2(20) NOT NULL,
    nazwisko           VARCHAR2(30) NOT NULL,
    kraj_pochodzenia   VARCHAR2(30)
);

CREATE INDEX autor__idx_nazw ON
    autor (
        nazwisko
    ASC );

ALTER TABLE autor ADD CONSTRAINT autor_pk PRIMARY KEY ( id_autora );

CREATE TABLE autor_produktu (
    produkt_nazwa     VARCHAR2(30) NOT NULL,
    autor_id_autora   INTEGER NOT NULL
);

CREATE INDEX autor_produktu__idx_autor ON
    autor_produktu (
        autor_id_autora
    ASC );

ALTER TABLE autor_produktu ADD CONSTRAINT autor_produktu_pk PRIMARY KEY ( produkt_nazwa,
                                                                          autor_id_autora );

CREATE TABLE element_koszyka (
    lp                           INTEGER NOT NULL,
    liczba                       INTEGER NOT NULL,
    wartosc_artykulu             FLOAT(2) NOT NULL,
    koszyk_zakupowy_nr_koszyka   INTEGER NOT NULL,
    produkt_nazwa                VARCHAR2(30) NOT NULL
);

ALTER TABLE element_koszyka ADD CONSTRAINT element_koszyka_pk PRIMARY KEY ( lp,
                                                                            koszyk_zakupowy_nr_koszyka );

CREATE TABLE gra_planszowa (
    nazwa          VARCHAR2(30) NOT NULL,
    g_min_gracze   INTEGER NOT NULL,
    g_max_gracze   INTEGER NOT NULL,
    g_min_wiek     INTEGER,
    g_czas_gry     INTEGER
);

ALTER TABLE gra_planszowa ADD CONSTRAINT gra_planszowa_ck_1 CHECK ( g_min_gracze <= g_max_gracze );

ALTER TABLE gra_planszowa ADD CONSTRAINT gra_planszowa_pk PRIMARY KEY ( nazwa );

CREATE TABLE klient (
    login           VARCHAR2(30) NOT NULL,
    k_adres_email   VARCHAR2(30) NOT NULL,
    k_adres         VARCHAR2(50) NOT NULL
);

ALTER TABLE klient ADD CONSTRAINT klient_pk PRIMARY KEY ( login );

CREATE TABLE koszyk_zakupowy (
    klient_login                   VARCHAR2(30) NOT NULL,
    nr_koszyka                     INTEGER NOT NULL,
    wartosc_zakupow                FLOAT(2) NOT NULL,
    sposob_platnosci               VARCHAR2(20) NOT NULL,
    koszt_wysylki                  FLOAT(2) NOT NULL,
    calkowita_wartosc_zamowienia   FLOAT(2) NOT NULL
);

CREATE INDEX koszyk_zakupowy__idx_klient ON
    koszyk_zakupowy (
        klient_login
    ASC );

ALTER TABLE koszyk_zakupowy ADD CONSTRAINT koszyk_zakupowy_pk PRIMARY KEY ( nr_koszyka );

CREATE TABLE ksiazka (
    nazwa            VARCHAR2(30) NOT NULL,
    k_typ_okladki    VARCHAR2(10),
    k_liczba_stron   INTEGER,
    k_format         VARCHAR2(4),
    seria_tytul      VARCHAR2(50)
);

ALTER TABLE ksiazka ADD CONSTRAINT ksi��ka_pk PRIMARY KEY ( nazwa );

CREATE TABLE pracownik (
    login                 VARCHAR2(30) NOT NULL,
    p_data_zatrudnienia   DATE NOT NULL,
    p_pensja_brutto       FLOAT(2) NOT NULL,
    p_stanowisko          VARCHAR2(20) DEFAULT ON NULL 'magazynier' NOT NULL,
    p_typ_umowy           VARCHAR2(30) DEFAULT ON NULL 'praca' NOT NULL
);

ALTER TABLE pracownik
    ADD CHECK ( p_stanowisko IN (
        'kierownik',
        'magazynier'
    ) );

ALTER TABLE pracownik
    ADD CHECK ( p_typ_umowy IN (
        'praca',
        'zlecenie'
    ) );

ALTER TABLE pracownik ADD CONSTRAINT pracownik_pk PRIMARY KEY ( login );

CREATE TABLE produkt (
    nazwa               VARCHAR2(30) NOT NULL,
    cena                FLOAT(2) NOT NULL,
    rok_wydania         DATE,
    stan_magazyn        INTEGER NOT NULL,
    wydawnictwo_nazwa   VARCHAR2(30) NOT NULL,
    co                  VARCHAR2(1) NOT NULL
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

CREATE INDEX produkt__idx_wydawnictwo ON
    produkt (
        wydawnictwo_nazwa
    ASC );

ALTER TABLE produkt ADD CONSTRAINT produkt_pk PRIMARY KEY ( nazwa );

CREATE TABLE seria (
    tytul          VARCHAR2(50) NOT NULL,
    liczba_tomow   INTEGER
);

ALTER TABLE seria ADD CONSTRAINT seria_pk PRIMARY KEY ( tytul );

CREATE TABLE uzytkownik (
    login           VARCHAR2(30) NOT NULL,
    haslo           VARCHAR2(20) NOT NULL,
    imie            VARCHAR2(20) NOT NULL,
    nazwisko        VARCHAR2(30) NOT NULL,
    nr_kontaktowy   VARCHAR2(9),
    kto             VARCHAR2(1) NOT NULL
);

ALTER TABLE uzytkownik
    ADD CONSTRAINT ch_inh_u�ytkownik CHECK ( kto IN (
        'k',
        'p'
    ) );

CREATE INDEX u�ytkownik__idx ON
    uzytkownik (
        login
    ASC );

ALTER TABLE uzytkownik ADD CONSTRAINT u�ytkownik_pk PRIMARY KEY ( login );

CREATE TABLE wydawnictwo (
    nazwa              VARCHAR2(30) NOT NULL,
    kraj_pochodzenia   VARCHAR2(30)
);

ALTER TABLE wydawnictwo ADD CONSTRAINT wydawnictwo_pk PRIMARY KEY ( nazwa );

ALTER TABLE autor_produktu
    ADD CONSTRAINT autor_produktu_autor_fk FOREIGN KEY ( autor_id_autora )
        REFERENCES autor ( id_autora );

ALTER TABLE autor_produktu
    ADD CONSTRAINT autor_produktu_produkt_fk FOREIGN KEY ( produkt_nazwa )
        REFERENCES produkt ( nazwa )
            ON DELETE CASCADE;

ALTER TABLE element_koszyka
    ADD CONSTRAINT element_koszyk_zakupowy_fk FOREIGN KEY ( koszyk_zakupowy_nr_koszyka )
        REFERENCES koszyk_zakupowy ( nr_koszyka )
            ON DELETE CASCADE;

ALTER TABLE element_koszyka
    ADD CONSTRAINT element_koszyka_produkt_fk FOREIGN KEY ( produkt_nazwa )
        REFERENCES produkt ( nazwa );

ALTER TABLE gra_planszowa
    ADD CONSTRAINT gra_planszowa_produkt_fk FOREIGN KEY ( nazwa )
        REFERENCES produkt ( nazwa )
            ON DELETE CASCADE;

ALTER TABLE klient
    ADD CONSTRAINT klient_u�ytkownik_fk FOREIGN KEY ( login )
        REFERENCES uzytkownik ( login )
            ON DELETE CASCADE;

ALTER TABLE koszyk_zakupowy
    ADD CONSTRAINT koszyk_zakupowy_klient_fk FOREIGN KEY ( klient_login )
        REFERENCES klient ( login );

ALTER TABLE ksiazka
    ADD CONSTRAINT ksi��ka_produkt_fk FOREIGN KEY ( nazwa )
        REFERENCES produkt ( nazwa )
            ON DELETE CASCADE;

ALTER TABLE ksiazka
    ADD CONSTRAINT ksi��ka_seria_fk FOREIGN KEY ( seria_tytul )
        REFERENCES seria ( tytul )
            ON DELETE SET NULL;

ALTER TABLE pracownik
    ADD CONSTRAINT pracownik_u�ytkownik_fk FOREIGN KEY ( login )
        REFERENCES uzytkownik ( login )
            ON DELETE CASCADE;

ALTER TABLE produkt
    ADD CONSTRAINT produkt_wydawnictwo_fk FOREIGN KEY ( wydawnictwo_nazwa )
        REFERENCES wydawnictwo ( nazwa );

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
        raise_application_error(-20223, 'FK Pracownik_U�ytkownik_FK in Table Pracownik violates Arc constraint on Table Uzytkownik - discriminator column kto doesn''t have value ''p'''
        );
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
        raise_application_error(-20223, 'FK Klient_U�ytkownik_FK in Table Klient violates Arc constraint on Table Uzytkownik - discriminator column kto doesn''t have value ''k'''
        );
    END IF;

EXCEPTION
    WHEN no_data_found THEN
        NULL;
    WHEN OTHERS THEN
        RAISE;
END;
/

CREATE OR REPLACE TRIGGER arc_fkarc_1_gra_planszowa BEFORE
    INSERT OR UPDATE OF nazwa ON gra_planszowa
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
        a.nazwa = :new.nazwa;

    IF ( d IS NULL OR d <> 'g' ) THEN
        raise_application_error(-20223, 'FK Gra_planszowa_Produkt_FK in Table Gra_planszowa violates Arc constraint on Table Produkt - discriminator column co doesn''t have value ''g'''
        );
    END IF;

EXCEPTION
    WHEN no_data_found THEN
        NULL;
    WHEN OTHERS THEN
        RAISE;
END;
/

CREATE OR REPLACE TRIGGER arc_fkarc_1_ksiazka BEFORE
    INSERT OR UPDATE OF nazwa ON ksiazka
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
        a.nazwa = :new.nazwa;

    IF ( d IS NULL OR d <> 'k' ) THEN
        raise_application_error(-20223, 'FK Ksi��ka_Produkt_FK in Table Ksiazka violates Arc constraint on Table Produkt - discriminator column co doesn''t have value ''k'''
        );
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



-- Oracle SQL Developer Data Modeler Summary Report: 
-- 
-- CREATE TABLE                            12
-- CREATE INDEX                             7
-- ALTER TABLE                             28
-- CREATE VIEW                              0
-- ALTER VIEW                               0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- CREATE TRIGGER                           6
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
-- CREATE SEQUENCE                          2
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