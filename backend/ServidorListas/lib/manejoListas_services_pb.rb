# Generated by the protocol buffer compiler.  DO NOT EDIT!
# Source: manejoListas.proto for package 'mnjListas'

require 'grpc'
require 'manejoListas_pb'

module MnjListas
  module ManejoListas
    class Service

      include GRPC::GenericService

      self.marshal_class_method = :encode
      self.unmarshal_class_method = :decode
      self.service_name = 'mnjListas.ManejoListas'

      rpc :AgregarQuitarCancionesLista, ListaAA, RespuestaListas
      rpc :AgregarListaAMeGusta, ListaAG, RespuestaListas
      rpc :BuscarListas, NombreLista, ListaListas
      rpc :ConsultarMisListas, IdUsuario, ListaListas
      rpc :ConsultarMisListasAgregadas, IdUsuario, ListaListas
      rpc :ConsultarMisListasDefault, IdUsuario, ListaListas
      rpc :CrearListaReproduccion, ListaAG, RespuestaListas
      rpc :EliminarListaReproduccion, ListaAG, RespuestaListas
    end

    Stub = Service.rpc_stub_class
  end
end