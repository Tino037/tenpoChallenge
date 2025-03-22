-- Otorgar todos los privilegios al usuario en el esquema public
GRANT ALL ON SCHEMA public TO tenpo_user;
GRANT ALL ON ALL TABLES IN SCHEMA public TO tenpo_user;
GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO tenpo_user;

-- Establecer el esquema de búsqueda predeterminado
ALTER USER tenpo_user SET search_path TO public;

-- Asegurar que los nuevos objetos creados pertenezcan al usuario
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO tenpo_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO tenpo_user; 