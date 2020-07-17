#!/usr/bin/env ruby
# -*- coding: utf-8 -*-
this_dir = File.expand_path(File.dirname(__FILE__))
lib_dir = File.join(this_dir, 'lib')
$LOAD_PATH.unshift(lib_dir) unless $LOAD_PATH.include?(lib_dir)

require 'grpc'
require 'manejoCuentas_services_pb'
require 'tiny_tds'
require 'time'
require 'queries_sql'

# implementar la clase generada
class ManejoCuentasImpl < MnjCuentas::ManejoCuentas::Service

  @@cliente_db

  def initialize
    begin
      @@cliente_db = TinyTds::Client.new host: '127.0.0.1', port: 1433, database: 'SiReMu', username: 'SiReMuServer', password: 'Servidor2020SiReMu5129'
    rescue => exception
      puts exception
    end
  end
  
  def iniciar_sesion(usuario, _unused_call)
    sql_buscar_usuario = QueriesSQL.get_sql_buscar_usuario(usuario.usuario, usuario.contrasenia)
    begin
      resultado = @@cliente_db.execute(sql_buscar_usuario)
      if resultado.count == 0
        return MnjCuentas::RespuestaCuentas.new(respuesta: false)
      end
    rescue StandardError => exception
      puts(exception)
      raise GRPC::BadStatus.new(GRPC::Core::StatusCodes::INTERNAL, 'ServerDataBaseException')
    end
    MnjCuentas::RespuestaCuentas.new(respuesta: true, codigoError: 'resultado')
  end
  
  def crear_lista_reproduccion(nueva_lista,  _unused_call)
    sql_crear_lista = QueriesSQL.get_sql_crear_lista(nueva_lista.idUsuario, nueva_lista.listaAAgregar.nombre, 
      nueva_lista.listaAAgregar.esPublica, ilustracion, nueva_lista.listaAAgregar.descripcion)
    begin
      File.open("/IlustracionesListas/#{nueva_lista.idUsuario}#{nueva_lista.listaAAgregar.nombre}#{nueva_lista.listaAAgregar.extensionIlustracion}", 'wb') do |salida|
        salida.write(nueva_lista.listaAAgregar.ilustracion)
      end
      @@cliente_db.execute(sql_crear_lista)
    rescue StandardError => exception
      puts(exception)
      return MnjListas::RespuestaListas.new(respuesta: false, codigoError: "0001")
    end
    #begin
    #  File.open("/IlustracionesListas/#{nueva_lista.idUsuario}#{nueva_lista.listaAAgregar.nombre}.jpg", 'wb') do |salida|
    #    salida.write(nueva_lista.listaAAgregar.ilustracion)
    #  end
    #rescue => exception
    #  puts(exception)
    #end
    MnjListas::RespuestaListas.new(respuesta: true)
  end
  
  def crear_lista_reproduccion(nueva_lista,  _unused_call)
    
    MnjListas::RespuestaListas.new(respuesta: true)
	end

end

def main
  port = '127.0.0.1:25113'
  s = GRPC::RpcServer.new
  s.add_http2_port(port, :this_port_is_insecure)
  GRPC.logger.info("... corriendo inseguramente en el puerto: #{port}")
  puts "Empieza el servidor a las:  #{Time.new}"
  s.handle(ManejoCuentasImpl)
  # Runs the server with SIGHUP, SIGINT and SIGQUIT signal handlers to 
  #   gracefully shutdown.
  # User could also choose to run server via call to run_till_terminated
  s.run_till_terminated
end

main