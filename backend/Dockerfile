FROM ubuntu:20.04
RUN apt-get update
RUN DEBIAN_FRONTEND="noninteractive" apt-get -y install tzdata
RUN apt -y install git
RUN apt-get -y  install ruby-full
RUN gem install grpc
RUN gem install grpc-tools
RUN apt-get install -y freetds-dev
RUN apt-get install -y wget
RUN apt-get update
RUN apt-get install -y build-essential
RUN apt-get install -y libc6-dev
RUN gem install tiny_tds
COPY ./backend /usr/home/
#Agregar java
RUN apt-get update && \
apt-get install -y openjdk-8-jdk && \
apt-get install -y ant && \
apt-get clean;
