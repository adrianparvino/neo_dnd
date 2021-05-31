{
  database = {pkgs, config, ...}: {
    networking.firewall.allowedTCPPorts = [ config.services.postgresql.port ];
    deployment.targetEnv = "container";

    services.postgresql.authentication = ''
      host  all  all 0.0.0.0/0 scram-sha-256
    '';
    services.postgresql.enable = true;
    services.postgresql.enableTCPIP = true;
    services.postgresql.package = pkgs.postgresql_11;
    services.postgresql.settings.password_encryption = "scram-sha-256";
    services.postgresql.initialScript = pkgs.writeText "psql-init" ''
      CREATE USER neo_dnd PASSWORD 'neo_dnd';
      CREATE DATABASE neo_dnd;
      GRANT ALL PRIVILEGES ON DATABASE neo_dnd TO neo_dnd;
    '';
  };
}
