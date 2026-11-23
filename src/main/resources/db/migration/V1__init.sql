CREATE TABLE room (
    id            UUID         NOT NULL PRIMARY KEY,
    name          VARCHAR(120) NOT NULL,
    location      VARCHAR(255),
    capacity      INT          NOT NULL,
    active        BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE reservation (
    id            UUID         NOT NULL PRIMARY KEY,
    room_id       UUID         NOT NULL,
    responsible   VARCHAR(120) NOT NULL,
    start_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    end_at        TIMESTAMP WITH TIME ZONE NOT NULL,
    status        VARCHAR(20)  NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL,
    cancelled_at  TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_reservation_room FOREIGN KEY (room_id) REFERENCES room (id),
    CONSTRAINT chk_reservation_time CHECK (end_at > start_at),
    CONSTRAINT chk_reservation_status CHECK (status IN ('ACTIVE', 'CANCELLED'))
);

CREATE INDEX idx_reservation_room_time ON reservation (room_id, start_at, end_at);
CREATE INDEX idx_reservation_status ON reservation (status);
CREATE INDEX idx_reservation_start_at ON reservation (start_at);
