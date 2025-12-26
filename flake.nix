{
  description = "Truelayer - Spring Boot (Maven) dev environment with Java 25";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-24.05";
    nixpkgs-unstable.url = "github:NixOS/nixpkgs/nixpkgs-unstable";
  };

  outputs = { self, nixpkgs, nixpkgs-unstable }:
  let
    systems = [ "x86_64-linux" "aarch64-linux" "x86_64-darwin" "aarch64-darwin" ];
    forAllSystems = f: nixpkgs.lib.genAttrs systems (system: f system);
  in
  {
    devShells = forAllSystems (system:
      let
        pkgs = nixpkgs.legacyPackages.${system};
        unstable = nixpkgs-unstable.legacyPackages.${system};

        # Prefer Temurin 25 from nixpkgs (known attribute path)
        jdk25-temurin = nixpkgs.lib.attrByPath
          [ "javaPackages" "compiler" "temurin-bin" "jdk-25" ]
          null
          unstable;

        # Fallbacks (depending on channel/renames)
        jdk25-openjdk = nixpkgs.lib.attrByPath [ "openjdk25" ] null unstable;
        jdk25-short   = nixpkgs.lib.attrByPath [ "jdk25" ] null unstable;

        jdk25 =
          if jdk25-temurin != null then jdk25-temurin
          else if jdk25-openjdk != null then jdk25-openjdk
          else if jdk25-short != null then jdk25-short
          else throw "No Java 25 JDK found in nixpkgs-unstable. Try updating the lock file or adjusting the attribute name.";
      in
      {
        default = pkgs.mkShell {
          packages = [ jdk25 pkgs.maven pkgs.git ];

          shellHook = ''
            export JAVA_HOME=${jdk25}
            export MAVEN_OPTS="-Dmaven.repo.local=$PWD/.m2 -Xmx1g"

            echo "Dev shell ready (Java 25)"
            java -version
            mvn -version
          '';
        };
      }
    );
  };
}