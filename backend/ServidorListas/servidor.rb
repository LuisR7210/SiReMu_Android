#!/usr/bin/env ruby
# -*- coding: utf-8 -*-
this_dir = File.expand_path(File.dirname(__FILE__))
lib_dir = File.join(this_dir, 'lib')
$LOAD_PATH.unshift(lib_dir) unless $LOAD_PATH.include?(lib_dir)

require 'grpc'
require 'manejoListas_services_pb'
require 'tiny_tds'
require 'time'
require 'queries_sql'

# implementar la clase generada
class ManejoListasImpl < MnjListas::ManejoListas::Service

  @@cliente_db

  def initialize
    begin
      @@cliente_db = TinyTds::Client.new host: '127.0.0.1', port: 1433, database: 'SiReMu', username: 'SiReMuServer', password: 'Servidor2020SiReMu5129'
    rescue => exception
      puts exception
    end
  end
  
  def verificar_lista_usuario(id_lista, id_usuario)
    sql_verificar_lista_usuario = QueriesSQL.get_sql_verificar_lista_usuario(id_lista, id_usuario)
    begin
      resultado = @@cliente_db.execute(sql_verificar_lista_usuario)
      if resultado.count == 0
        return false
      end
    rescue StandardError => exception
      puts(exception)
      return false
    end
    true
  end

  def agregar_quitar_canciones_lista(lista_a_agregar,  _unused_call)
    lista_valida = verificar_lista_usuario(lista_a_agregar.idLista, lista_a_agregar.idUsuario)
    unless lista_valida
      return MnjListas::RespuestaListas.new(respuesta: false, codigoError: "0005")
    end
    lista_a_agregar.idCanciones.each do |id_cancion|
      if lista_a_agregar.agregar
        sql_a_ejecutar = QueriesSQL.get_sql_agregar_a_lista(lista_a_agregar.idLista, id_cancion)
      else
        sql_a_ejecutar = QueriesSQL.get_sql_quitar_cancion_de_lista(lista_a_agregar.idLista, id_cancion)
      end
      begin
        @@cliente_db.execute(sql_a_ejecutar)
      rescue StandardError => exception
        puts(exception)
        return MnjListas::RespuestaListas.new(respuesta: false, codigoError: "0001")
      end
    end
    MnjListas::RespuestaListas.new(respuesta: true)
  end
  
  def agregar_lista_a_me_gusta(lista_a_agregar,  _unused_call)
    sql_lista_me_gusta = QueriesSQL.get_sql_agregar_lista_a_me_gusta(lista_a_agregar.listaAAgregar.id, lista_a_agregar.idUsuario)
      begin
        @@cliente_db.execute(sql_lista_me_gusta)
      rescue StandardError => exception
        puts(exception)
        return MnjListas::RespuestaListas.new(respuesta: false, codigoError: "0001")
      end
    MnjListas::RespuestaListas.new(respuesta: true)
  end

  def buscar_listas(nombre_lista,  _unused_call)
    listas_reproduccion = []
    sql_buscar_listas = QueriesSQL.get_sql_buscar_listas(nombre_lista.nombre)
    begin
      resultados = @@cliente_db.execute(sql_buscar_listas)
      resultados.each do |fila|  
        imagen = File.binread("#{fila['ilustracion']}")
        listas_reproduccion << MnjListas::ListaReproduccion.new(id: fila['Id'], nombre: "#{fila['nombre']}", 
          esPublica: fila['esPublica'], likes: fila['likes'], ilustracion: imagen, descripcion: "#{fila['descripcion']}", 
          idCreador: fila['IdUsuario'], nombreCreador: "#{fila['usuario']}")
      end
    rescue StandardError => exception
      puts(exception)
    ensure
      return MnjListas::ListaListas.new(listas: listas_reproduccion)
    end
  end
  
  def consultar_mis_listas(id_usuario,  _unused_call)
    listas_usuario = []
    sql_consultar_mis_listas = QueriesSQL.get_sql_consultar_mis_listas(id_usuario.id)
    begin
      resultados = @@cliente_db.execute(sql_consultar_mis_listas)
      resultados.each do |fila|  
        imagen = File.binread("#{fila['ilustracion']}")
        listas_usuario << MnjListas::ListaReproduccion.new(id: fila['Id'], nombre: "#{fila['nombre']}", 
          esPublica: fila['esPublica'], likes: fila['likes'], ilustracion: imagen, descripcion: "#{fila['descripcion']}", 
          idCreador: fila['IdUsuario'], nombreCreador: "#{fila['usuario']}")
      end
    rescue StandardError => exception
      puts(exception)
    ensure
      return MnjListas::ListaListas.new(listas: listas_usuario)
    end
  end

  def consultar_mis_listas_agregadas(id_usuario,  _unused_call)
    listas_usuario = []
    sql_consultar_mis_listas = QueriesSQL.get_sql_consultar_mis_listas_agregadas(id_usuario.id)
    begin
      resultados = @@cliente_db.execute(sql_consultar_mis_listas)
      resultados.each do |fila|  
        imagen = File.binread("#{fila['ilustracion']}")
        listas_usuario << MnjListas::ListaReproduccion.new(id: fila['Id'], nombre: "#{fila['nombre']}", 
          esPublica: fila['esPublica'], likes: fila['likes'], ilustracion: imagen, descripcion: "#{fila['descripcion']}", 
          idCreador: fila['IdUsuario'], nombreCreador: "#{fila['usuario']}")
      end
    rescue StandardError => exception
      puts(exception)
    ensure
      return MnjListas::ListaListas.new(listas: listas_usuario)
    end
  end

  def consultar_mis_listas_default(id_usuario,  _unused_call)
    listas_usuario_default = []
    sql_consultar_listas_default = QueriesSQL.get_sql_consultar_mis_listas_default(id_usuario.id)
    begin
      resultados = @@cliente_db.execute(sql_consultar_listas_default)
      resultados.each do |fila|  
        listas_usuario_default << MnjListas::ListaReproduccion.new(id: fila['Id'], nombre: "#{fila['nombre']}", 
          esDefault: fila['esDefault'])
      end
    rescue StandardError => exception
      puts(exception)
    ensure
      return MnjListas::ListaListas.new(listas: listas_usuario_default)
    end
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
  port = '127.0.0.1:25111'
  s = GRPC::RpcServer.new
  s.add_http2_port(port, :this_port_is_insecure)
  GRPC.logger.info("... corriendo inseguramente en el puerto: #{port}")
  puts "Empieza el servidor a las:  #{Time.new}"
  s.handle(ManejoListasImpl)
  # Runs the server with SIGHUP, SIGINT and SIGQUIT signal handlers to 
  #   gracefully shutdown.
  # User could also choose to run server via call to run_till_terminated
  s.run_till_terminated
end

main