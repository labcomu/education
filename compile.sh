#!/bin/bash

do_local() {
  mvn clean install
}

do_docker() {
  ./compile_docker_stop.sh
  ./compile_docker_start.sh
  ./compile_docker_stop.sh
}

. ./do.sh Compile $@