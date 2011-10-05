CREATE TABLE TOPCAT_USER
(
  ID NUMBER NOT NULL,
  TOPCAT_USER_ID NUMBER NOT NULL,
  SERVER_ID NUMBER NOT NULL,
  NAME VARCHAR2(255),
  USER_SURNAME VARCHAR2(255),
  DN VARCHAR2(2000)
, CONSTRAINT TOPCAT_USER_PK PRIMARY KEY
  (
    ID
  )
  ENABLE
)
;

CREATE TABLE TOPCAT_KEYWORDS
(
  KEYWORD VARCHAR2(255 BYTE) NOT NULL
)
  TABLESPACE "USERS"
  LOGGING 
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
    INITIAL 64K
    MINEXTENTS 1
    MAXEXTENTS 2147483645
    BUFFER_POOL DEFAULT
  )
;

CREATE TABLE TOPCAT_USER_INFO
(
  ID NUMBER NOT NULL,
  DISPLAY_NAME VARCHAR2(255),
  HOME_SERVER NUMBER
, CONSTRAINT TOPCAT_USER_INFO_PK PRIMARY KEY
  (
    ID
  )
  ENABLE
)
;

CREATE TABLE TOPCAT_ICAT_SERVER
(
  ID NUMBER NOT NULL,
  NAME VARCHAR2(20),
  SERVER_URL VARCHAR2(255) NOT NULL,
  DEFAULT_USER VARCHAR2(20),
  DEFAULT_PASSWORD VARCHAR2(20),
  PLUGIN_NAME VARCHAR2(255),
  VERSION VARCHAR2(255)
, CONSTRAINT TOPCAT_ICATSERVER_PK PRIMARY KEY
  (
    ID
  )
  ENABLE
)
;

CREATE TABLE SEQUENCE
(
  SEQ_NAME VARCHAR2(50 BYTE) NOT NULL,
  SEQ_COUNT NUMBER(38, 0)
, CONSTRAINT SEQUENCE_PK PRIMARY KEY
  (
    SEQ_NAME
  )
  ENABLE
)
  TABLESPACE "USERS"
  LOGGING 
  PCTFREE 10
  INITRANS 1
  MAXTRANS 255
  STORAGE
  (
    INITIAL 64K
    MINEXTENTS 1
    MAXEXTENTS 2147483645
    BUFFER_POOL DEFAULT
  )
;

CREATE TABLE TOPCAT_USER_SESSION
(
  ID NUMBER NOT NULL,
  USER_ID NUMBER NOT NULL,
  TOPCAT_SESSION_ID VARCHAR2(255),
  ICAT_SESSION_ID VARCHAR2(255),
  EXPIRY_DATE TIMESTAMP
, CONSTRAINT TOPCAT_USER_SESSION_PK PRIMARY KEY
  (
    ID
  )
  ENABLE
)
;

CREATE TABLE TOPCAT_USER_DOWNLOAD
(
  ID NUMBER NOT NULL,
  USER_ID NUMBER NOT NULL,
  SUBMIT_TIME TIMESTAMP,
  EXPIRY_TIME TIMESTAMP,
  NAME VARCHAR2(255),
  STATUS VARCHAR2(20),
  URL VARCHAR2(255),
, CONSTRAINT TOPCAT_USER_DOWNLOAD_PK PRIMARY KEY
  (
    ID
  )
  ENABLE
)
;

ALTER TABLE TOPCAT_KEYWORDS
ADD CONSTRAINT KEYWORD_UQ UNIQUE
(
  KEYWORD
)
 ENABLE
;

ALTER TABLE TOPCAT_USER
ADD CONSTRAINT ICAT_USER_TOPCAT_USER_INF_FK1 FOREIGN KEY
(
  TOPCAT_USER_ID
)
REFERENCES TOPCAT_USER_INFO
(
  ID
)
 ENABLE
;

ALTER TABLE TOPCAT_USER
ADD CONSTRAINT TOPCAT_USER_TOPCAT_ICAT_S_FK1 FOREIGN KEY
(
  SERVER_ID
)
REFERENCES TOPCAT_ICAT_SERVER
(
  ID
)
ON DELETE CASCADE ENABLE
;

ALTER TABLE TOPCAT_USER_INFO
ADD CONSTRAINT TOPCAT_USER_INFO_TOPCAT_I_FK1 FOREIGN KEY
(
  HOME_SERVER
)
REFERENCES TOPCAT_ICAT_SERVER
(
  ID
)
 ENABLE
;

ALTER TABLE TOPCAT_USER_SESSION
ADD CONSTRAINT TOPCAT_USER_SESSION_TOPCA_FK1 FOREIGN KEY
(
  USER_ID
)
REFERENCES TOPCAT_USER
(
  ID
)
 ENABLE
;

CREATE INDEX IDX_TOPCAT_USER_DOWNLOAD ON TOPCAT_USER_DOWNLOAD (USER_ID ASC);

ALTER TABLE TOPCAT_USER_DOWNLOAD
ADD CONSTRAINT TOPCAT_USER_DOWNLOAD_FK FOREIGN KEY
(
  USER_ID
)
REFERENCES TOPCAT_USER
(
  ID
)
ON DELETE CASCADE ENABLE
;
