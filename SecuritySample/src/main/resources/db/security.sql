create MEMORY table users (
	user_id varchar(20) not null primary key,
	password varchar(100) not null,
	enabled boolean not null,
	user_name varchar(50) not null
);

create MEMORY table roles (
	authority varchar(50) not null primary key,
	role_name varchar(50)
);

create MEMORY table authorities (
	user_id varchar(20) not null,
	authority varchar(50) not null,
	primary key(user_id, authority),
	foreign key(user_id) references users(user_id),
	foreign key(authority) references roles(authority)
);

create MEMORY table roles_hierarchy (
	parent_role varchar(50) not null,
	child_role varchar(50) not null,
	primary key(parent_role, child_role),
	foreign key(parent_role) references roles(authority),
	foreign key(child_role) references roles(authority)
);

------------------------------
SET SCHEMA PUBLIC;
------------------------------

Insert into USERS (USER_ID,USER_NAME,PASSWORD,ENABLED) values ('admin','Admin.','8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918',true);
Insert into USERS (USER_ID,USER_NAME,PASSWORD,ENABLED) values ('user','User','04f8996da763b7a969b1028ee3007569eaf3a635486ddab211d512c85b9df8fb',true);
-- admin/admin
-- user/user

Insert into ROLES (AUTHORITY,ROLE_NAME) values ('ROLE_ADMIN','Administrator Role');
Insert into ROLES (AUTHORITY,ROLE_NAME) values ('ROLE_USER','Public user');
Insert into ROLES (AUTHORITY,ROLE_NAME) values ('ROLE_RESTRICTED','Restricted user');

Insert into AUTHORITIES (USER_ID,AUTHORITY) values ('admin','ROLE_ADMIN');
Insert into AUTHORITIES (USER_ID,AUTHORITY) values ('user','ROLE_USER');

Insert into ROLES_HIERARCHY (PARENT_ROLE,CHILD_ROLE) values ('ROLE_RESTRICTED','ROLE_USER');
Insert into ROLES_HIERARCHY (PARENT_ROLE,CHILD_ROLE) values ('ROLE_USER','ROLE_ADMIN');

------------------------------

create MEMORY table secured_resources (
	resource_id varchar(15) not null primary key,
	resource_name varchar(50),
	resource_pattern varchar(100) not null,
	resource_type varchar(10),
	sort_order integer
);

create MEMORY table secured_resources_role (
	resource_id varchar(15) not null,
	authority varchar(50) not null,
	primary key(resource_id, authority),
	foreign key(resource_id) references secured_resources(resource_id),
	foreign key(authority) references roles(authority)
);


Insert into SECURED_RESOURCES (RESOURCE_ID,RESOURCE_NAME,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) 
values ('WEB-0000001','Sample Add','/sample/add*','url',1);
Insert into SECURED_RESOURCES (RESOURCE_ID,RESOURCE_NAME,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) 
values ('WEB-0000002','Sample Update','/sample/update*','url',1);
Insert into SECURED_RESOURCES (RESOURCE_ID,RESOURCE_NAME,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) 
values ('WEB-0000003','Sample Delete','/sample/delete*','url',1);
Insert into SECURED_RESOURCES (RESOURCE_ID,RESOURCE_NAME,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) 
values ('WEB-0000004','Sample Select','/sample/egov*','url',2);
Insert into SECURED_RESOURCES (RESOURCE_ID,RESOURCE_NAME,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) 
values ('WEB-0000005','Default Page','/index.jsp','url',1);

Insert into SECURED_RESOURCES_ROLE (RESOURCE_ID,AUTHORITY) values ('WEB-0000001','ROLE_ADMIN');
Insert into SECURED_RESOURCES_ROLE (RESOURCE_ID,AUTHORITY) values ('WEB-0000002','ROLE_ADMIN');
Insert into SECURED_RESOURCES_ROLE (RESOURCE_ID,AUTHORITY) values ('WEB-0000003','ROLE_ADMIN');
Insert into SECURED_RESOURCES_ROLE (RESOURCE_ID,AUTHORITY) values ('WEB-0000004','ROLE_USER');
Insert into SECURED_RESOURCES_ROLE (RESOURCE_ID,AUTHORITY) values ('WEB-0000005','ROLE_USER');

-- method security testing
Insert into ROLES (AUTHORITY,ROLE_NAME) values ('ROLE_SUPER','Superiors Role');
Insert into ROLES_HIERARCHY (PARENT_ROLE,CHILD_ROLE) values ('ROLE_ADMIN','ROLE_SUPER');

Insert into SECURED_RESOURCES (RESOURCE_ID,RESOURCE_NAME,RESOURCE_PATTERN,RESOURCE_TYPE,SORT_ORDER) 
values ('MTD-0000001','Sample Insert','egovframework.rte.sample.service.EgovSampleService.insertSample','method',1);

Insert into SECURED_RESOURCES_ROLE (RESOURCE_ID,AUTHORITY) values ('MTD-0000001','ROLE_SUPER');

-- restore role mapping
--Update SECURED_RESOURCES_ROLE set AUTHORITY = 'ROLE_ADMIN'
--where RESOURCE_ID = 'MTD-0000001';

commit;
