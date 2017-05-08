--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.5
-- Dumped by pg_dump version 9.4.5
-- Started on 2017-04-21 12:45:37

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

DROP DATABASE twitter;
--
-- TOC entry 2019 (class 1262 OID 24723)
-- Name: twitter; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE twitter WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'Portuguese_Brazil.1252' LC_CTYPE = 'Portuguese_Brazil.1252';


ALTER DATABASE twitter OWNER TO postgres;

\connect twitter

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 5 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO postgres;

--
-- TOC entry 2020 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 177 (class 3079 OID 11855)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2022 (class 0 OID 0)
-- Dependencies: 177
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 173 (class 1259 OID 24726)
-- Name: hashtag; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hashtag (
    hashtag_id integer NOT NULL,
    hashtag text NOT NULL
);


ALTER TABLE hashtag OWNER TO postgres;

--
-- TOC entry 172 (class 1259 OID 24724)
-- Name: hashtag_id_hashtag_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hashtag_id_hashtag_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE hashtag_id_hashtag_id_seq OWNER TO postgres;

--
-- TOC entry 2023 (class 0 OID 0)
-- Dependencies: 172
-- Name: hashtag_id_hashtag_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE hashtag_id_hashtag_id_seq OWNED BY hashtag.hashtag_id;


--
-- TOC entry 175 (class 1259 OID 24739)
-- Name: tweet; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tweet (
    tweet_id bigint NOT NULL,
    id_user bigint NOT NULL,
    text text,
    source text,
    lang text,
    created_at timestamp without time zone,
    coordinates_longitude numeric,
    coordinates_latitude numeric
);


ALTER TABLE tweet OWNER TO postgres;

--
-- TOC entry 176 (class 1259 OID 24752)
-- Name: tweet_hashtag; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE tweet_hashtag (
    tweet_id bigint NOT NULL,
    hashtag_id bigint NOT NULL
);


ALTER TABLE tweet_hashtag OWNER TO postgres;

--
-- TOC entry 174 (class 1259 OID 24734)
-- Name: user_twitter; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE user_twitter (
    id bigint NOT NULL,
    name text NOT NULL,
    screen_name text NOT NULL,
    location text NOT NULL,
    created_at timestamp without time zone,
    utc_offset integer,
    time_zone text,
    lang text
);


ALTER TABLE user_twitter OWNER TO postgres;

--
-- TOC entry 1896 (class 2604 OID 24729)
-- Name: hashtag_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hashtag ALTER COLUMN hashtag_id SET DEFAULT nextval('hashtag_id_hashtag_id_seq'::regclass);


--
-- TOC entry 1898 (class 2606 OID 24733)
-- Name: hashtag_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hashtag
    ADD CONSTRAINT hashtag_pkey PRIMARY KEY (hashtag_id);


--
-- TOC entry 1902 (class 2606 OID 24746)
-- Name: tweet_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY tweet
    ADD CONSTRAINT tweet_pkey PRIMARY KEY (tweet_id);


--
-- TOC entry 1900 (class 2606 OID 24738)
-- Name: user_twitter_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY user_twitter
    ADD CONSTRAINT user_twitter_pkey PRIMARY KEY (id);


--
-- TOC entry 1904 (class 2606 OID 24755)
-- Name: tweet_hashtag_hashtag_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tweet_hashtag
    ADD CONSTRAINT tweet_hashtag_hashtag_id_fkey FOREIGN KEY (hashtag_id) REFERENCES hashtag(hashtag_id);


--
-- TOC entry 1905 (class 2606 OID 24760)
-- Name: tweet_hashtag_tweet_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tweet_hashtag
    ADD CONSTRAINT tweet_hashtag_tweet_id_fkey FOREIGN KEY (tweet_id) REFERENCES tweet(tweet_id);


--
-- TOC entry 1903 (class 2606 OID 24747)
-- Name: tweet_id_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tweet
    ADD CONSTRAINT tweet_id_user_fkey FOREIGN KEY (id_user) REFERENCES user_twitter(id);


--
-- TOC entry 2021 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2017-04-21 12:45:37

--
-- PostgreSQL database dump complete
--

