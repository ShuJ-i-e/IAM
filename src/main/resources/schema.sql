CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
	username VARCHAR(255),
    firstName VARCHAR(255),
    lastName VARCHAR(255),
    email VARCHAR(255),
    about TEXT,
    roles VARCHAR(255),
    languages VARCHAR(255),
    skills VARCHAR(255),
    project_experiences TEXT,
    assignments TEXT,
    profilePic VARCHAR(255),
    password VARCHAR(255),
    reset_password_token VARCHAR(255),
    verificationToken VARCHAR(255),
    verificationTokenCreationTime TIMESTAMP,
    verified boolean
);
