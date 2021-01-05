use sidecar_db;

insert into users (email, password, password_confirm, photo_url, username, zipcode)
VALUES ('donelle@email.com',
        '$2a$10$HI8FrzvElf5wXpC6rNJDN.S9ed6MibGqQL3ucY0zCMJeLJFFvY8YS',
        '$2a$10$HI8FrzvElf5wXpC6rNJDN.S9ed6MibGqQL3ucY0zCMJeLJFFvY8YS',
        'https://cdn.filestackcontent.com/dcTiTSKTtGV8oTS8kzug',
        'donelle',
        '78245');


insert into e_categories (name)
values ('Club Event'),
       ('Group Ride'),
       ('Charity Event'),
       ('Fundraiser'),
       ('Looking to ride');

insert into p_categories (name, icon_name)
values ('Bar', 'bar'),
       ('Restaurant', 'restaurant'),
       ('Repair Shop', 'repair_shop'),
       ('Dealership', 'motorcycle_shop'),
       ('Scenic View', 'scenic_view'),
       ('Other', 'other');