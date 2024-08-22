--liquibase formatted sql

--changeset vershinin:1 labels:v0.0.1
CREATE TYPE "event_visibilities_type" AS ENUM ('public', 'owner', 'group');

CREATE TABLE "events" (
	"id" text primary key constraint events_id_length_ctr check (length("id") < 64),
	"title" text constraint events_title_length_ctr check (length(title) < 128),
	"description" text constraint events_description_length_ctr check (length(title) < 4096),
	"start" text constraint events_start_length_ctr check (length(title) < 21),
	"end" text constraint events_end_length_ctr check (length(title) < 21),
	"visibility" event_visibilities_type not null,
	"owner_id" text not null constraint events_owner_id_length_ctr check (length(id) < 64),
	"lock" text not null constraint events_lock_length_ctr check (length(id) < 64)
);

CREATE INDEX events_owner_id_idx on "events" using hash ("owner_id");

CREATE INDEX events_visibility_idx on "events" using hash ("visibility");
