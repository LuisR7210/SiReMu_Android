module QueriesSQL extend self
  
  def get_sql_verificar_lista_usuario(id_lista, id_usuario)
    return "SELECT * FROM UsuarioListaReproduccion 
    WHERE esPropietario = 1 AND Usuario_Id = #{id_usuario} AND ListaReproduccion_Id = #{id_lista}"
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

  def get_sql_agregar_lista_a_me_gusta(id_lista, id_usuario)
    return "IF NOT EXISTS (SELECT * FROM UsuarioListaReproduccion WHERE Usuario_Id = #{id_usuario} AND ListaReproduccion_Id = #{id_lista})
    BEGIN
      INSERT INTO UsuarioListaReproduccion VALUES (#{id_usuario}, #{id_lista}, 0)
      UPDATE ListaReproduccionSet SET likes += 1 WHERE Id = #{id_lista}
    END"
  end

  def get_sql_buscar_listas(nombre_lista)
    return "SELECT l.*, u.id AS IdUsuario, u.usuario FROM ListaReproduccionSet l 
    INNER JOIN (UsuarioListaReproduccion ul RIGHT JOIN UsuarioSet u ON u.Id = ul.Usuario_Id) ON l.Id = ul.ListaReproduccion_Id
    WHERE l.esPublica = 1 AND ul.esPropietario = 1 AND l.esDefault = 0 AND l.nombre LIKE '%#{nombre_lista}%'"
  end

  def get_sql_consultar_lista(id_lista)
    return "SELECT c.*, a.Id AS IdAlbum, a.nombre AS nombreAlbum  
    FROM ListaReproduccionCancion lc 
    INNER JOIN (CancionSet c LEFT JOIN AlbumSet a ON a.Id = c.Album_Id) ON lc.Cancion_Id = c.Id
    WHERE lc.ListaReproduccion_Id = #{id_lista}"
  end

  def get_sql_consultar_mis_listas(id_usuario)
    return "SELECT l.*, u.Id AS IdUsuario, u.usuario FROM ListaReproduccionSet l 
    INNER JOIN (UsuarioListaReproduccion ul INNER JOIN UsuarioSet u ON u.Id = ul.Usuario_Id) ON l.Id = ul.ListaReproduccion_Id
    WHERE ul.esPropietario = 1 AND l.esDefault = 0 AND ul.Usuario_Id = #{id_usuario}"
  end

  def get_sql_consultar_mis_listas_agregadas(id_usuario)
    return "SELECT l.*, c.IdUsuario, c.usuario FROM ListaReproduccionSet l 
    INNER JOIN UsuarioListaReproduccion ul ON l.Id = ul.ListaReproduccion_Id
	  LEFT JOIN (
      SELECT l.Id AS IdLista, u.Id AS IdUsuario, u.usuario FROM ListaReproduccionSet l 
      INNER JOIN (UsuarioListaReproduccion ul INNER JOIN UsuarioSet u ON u.Id = ul.Usuario_Id) ON l.Id = ul.ListaReproduccion_Id
      WHERE l.esPublica = 1 AND ul.esPropietario = 1 AND l.esDefault = 0
	  ) AS c ON c.IdLista = l.Id
    WHERE l.esPublica = 1 AND ul.esPropietario = 0 AND l.esDefault = 0 AND ul.Usuario_Id = #{id_usuario}"
  end

  def get_sql_consultar_mis_listas_default(id_usuario)
    return "SELECT l.Id, l.esDefault, l.nombre 
    FROM ListaReproduccionSet l LEFT JOIN UsuarioListaReproduccion ul ON ul.ListaReproduccion_Id = l.Id 
    WHERE l.esDefault = 1 AND ul.esPropietario = 1 AND ul.Usuario_Id = #{id_usuario}"
  end

  def get_sql_crear_lista(id_usuario, nombre, publica, ilustracion, descripcion)
    return "DECLARE @idLista INT
    IF NOT EXISTS (SELECT * FROM ListaReproduccionSet WHERE nombre = #{nombre})
      BEGIN
        INSERT INTO ListaReproduccionCancion VALUES (0, #{nombre}, #{publica}, #{ilustracion}, 0, #{descripcion})
        SET @idLista = SCOPE_IDENTITY()
        INSERT INTO UsuarioListaReproduccion VALUES (#{id_usuario}, @idLista, 0)
      END"
  end

end 