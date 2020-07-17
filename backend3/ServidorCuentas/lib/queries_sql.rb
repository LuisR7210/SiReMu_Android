module QueriesSQL extend self
  
  def get_sql_buscar_usuario(usuario, contrasena)
    return "SELECT * FROM UsuarioSet WHERE usuario = '#{usuario}' AND contrasena = '#{contrasena}'"
  end

  def get_sql_agregar_a_lista(id_lista, id_cancion)
    return "IF NOT EXISTS (SELECT * FROM ListaReproduccionCancion 
      WHERE ListaReproduccion_Id = #{id_lista} AND Cancion_Id = #{id_cancion})
    BEGIN
      INSERT INTO ListaReproduccionCancion VALUES (#{id_lista}, #{id_cancion})
      IF EXISTS (SELECT * FROM ListaReproduccionSet WHERE nombre = 'Me gusta' AND Id = #{id_lista})
		    UPDATE CancionSet SET likes += 1 WHERE Id = #{id_cancion}
    END"
  end

  def get_sql_quitar_cancion_de_lista(id_lista, id_cancion)
    return "IF EXISTS (SELECT * FROM ListaReproduccionCancion 
      WHERE ListaReproduccion_Id = #{id_lista} AND Cancion_Id = #{id_cancion})
        BEGIN
          DELETE FROM ListaReproduccionCancion WHERE ListaReproduccion_Id = #{id_lista} AND Cancion_Id = #{id_cancion} 
          IF EXISTS (SELECT * FROM ListaReproduccionSet WHERE nombre = 'Me gusta' AND Id = #{id_lista})
            UPDATE CancionSet SET likes -= 1 WHERE Id = #{id_cancion}
        END"
  end

end 