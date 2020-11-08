FROM php:7.2.1-apache
RUN docker-php-ext-install pdo pdo_mysql mysqli

RUN apt-get update -y
RUN apt-get install libyaml-dev -y
RUN pecl install yaml && echo "extension=yaml.so" > /usr/local/etc/php/conf.d/ext-yaml.ini && docker-php-ext-enable yaml
