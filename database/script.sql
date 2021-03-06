USE [master]
GO
/****** Object:  Database [SiReMu]    Script Date: 06/07/2020 01:34:37 p. m. ******/
CREATE DATABASE [SiReMu]

ALTER DATABASE [SiReMu] SET COMPATIBILITY_LEVEL = 140
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [SiReMu].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [SiReMu] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [SiReMu] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [SiReMu] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [SiReMu] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [SiReMu] SET ARITHABORT OFF 
GO
ALTER DATABASE [SiReMu] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [SiReMu] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [SiReMu] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [SiReMu] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [SiReMu] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [SiReMu] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [SiReMu] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [SiReMu] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [SiReMu] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [SiReMu] SET  DISABLE_BROKER 
GO
ALTER DATABASE [SiReMu] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [SiReMu] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [SiReMu] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [SiReMu] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [SiReMu] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [SiReMu] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [SiReMu] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [SiReMu] SET RECOVERY FULL 
GO
ALTER DATABASE [SiReMu] SET  MULTI_USER 
GO
ALTER DATABASE [SiReMu] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [SiReMu] SET DB_CHAINING OFF 
GO
ALTER DATABASE [SiReMu] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [SiReMu] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [SiReMu] SET DELAYED_DURABILITY = DISABLED 
GO
EXEC sys.sp_db_vardecimal_storage_format N'SiReMu', N'ON'
GO
ALTER DATABASE [SiReMu] SET QUERY_STORE = OFF
GO
USE [SiReMu]
GO
/****** Object:  User [SiReMuServer]    Script Date: 06/07/2020 01:34:37 p. m. ******/
CREATE USER [SiReMuServer] FOR LOGIN [SiReMuServer] WITH DEFAULT_SCHEMA=[dbo]
GO
ALTER ROLE [db_datareader] ADD MEMBER [SiReMuServer]
GO
ALTER ROLE [db_datawriter] ADD MEMBER [SiReMuServer]
GO
/****** Object:  Table [dbo].[AlbumSet]    Script Date: 06/07/2020 01:34:37 p. m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[AlbumSet](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[companiaDisco] [nvarchar](50) NOT NULL,
	[fechaLanzamiento] [date] NOT NULL,
	[ilustracion] [nvarchar](max) NOT NULL,
	[nombre] [nvarchar](100) NOT NULL,
	[Usuario_Id] [int] NOT NULL,
 CONSTRAINT [PK_AlbumSet] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CancionSet]    Script Date: 06/07/2020 01:34:37 p. m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CancionSet](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[archivo] [nvarchar](max) NOT NULL,
	[artista] [nvarchar](100) NOT NULL,
	[duracion] [time](7) NOT NULL,
	[genero] [nvarchar](50) NOT NULL,
	[nombre] [nvarchar](100) NOT NULL,
	[esPromocion] [bit] NOT NULL,
	[esPublica] [bit] NOT NULL,
	[Album_Id] [int] NOT NULL,
	[likes] [int] NOT NULL,
 CONSTRAINT [PK_CancionSet] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ListaReproduccionCancion]    Script Date: 06/07/2020 01:34:37 p. m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ListaReproduccionCancion](
	[ListaReproduccion_Id] [int] NOT NULL,
	[Cancion_Id] [int] NOT NULL,
 CONSTRAINT [PK_ListaReproduccionCancion] PRIMARY KEY CLUSTERED 
(
	[ListaReproduccion_Id] ASC,
	[Cancion_Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ListaReproduccionSet]    Script Date: 06/07/2020 01:34:37 p. m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ListaReproduccionSet](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[esDefault] [bit] NOT NULL,
	[nombre] [nvarchar](100) NOT NULL,
	[esPublica] [bit] NOT NULL,
	[ilustracion] [nvarchar](max) NULL,
	[likes] [int] NULL,
	[descripcion] [nvarchar](300) NULL,
 CONSTRAINT [PK_ListaReproduccionSet] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[UsuarioListaReproduccion]    Script Date: 06/07/2020 01:34:37 p. m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[UsuarioListaReproduccion](
	[Usuario_Id] [int] NOT NULL,
	[ListaReproduccion_Id] [int] NOT NULL,
	[esPropietario] [bit] NOT NULL,
 CONSTRAINT [PK_UsuarioListaReproduccion] PRIMARY KEY CLUSTERED 
(
	[Usuario_Id] ASC,
	[ListaReproduccion_Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[UsuarioSet]    Script Date: 06/07/2020 01:34:37 p. m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[UsuarioSet](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[contrasena] [nvarchar](50) NOT NULL,
	[nombre] [nvarchar](50) NOT NULL,
	[correo] [nvarchar](70) NOT NULL,
	[esCreadorContenido] [bit] NOT NULL,
	[fechaNacimiento] [date] NOT NULL,
	[genero] [nvarchar](50) NOT NULL,
	[usuario] [nvarchar](50) NOT NULL,
	[apellido] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_UsuarioSet] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Index [IX_FK_CancionAlbum]    Script Date: 06/07/2020 01:34:37 p. m. ******/
CREATE NONCLUSTERED INDEX [IX_FK_CancionAlbum] ON [dbo].[CancionSet]
(
	[Album_Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [IX_FK_ListaReproduccionCancion_Cancion]    Script Date: 06/07/2020 01:34:37 p. m. ******/
CREATE NONCLUSTERED INDEX [IX_FK_ListaReproduccionCancion_Cancion] ON [dbo].[ListaReproduccionCancion]
(
	[Cancion_Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [IX_FK_UsuarioListaReproduccion_ListaReproduccion]    Script Date: 06/07/2020 01:34:37 p. m. ******/
CREATE NONCLUSTERED INDEX [IX_FK_UsuarioListaReproduccion_ListaReproduccion] ON [dbo].[UsuarioListaReproduccion]
(
	[ListaReproduccion_Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
ALTER TABLE [dbo].[AlbumSet]  WITH CHECK ADD  CONSTRAINT [FK_AlbumSet_UsuarioSet] FOREIGN KEY([Usuario_Id])
REFERENCES [dbo].[UsuarioSet] ([Id])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[AlbumSet] CHECK CONSTRAINT [FK_AlbumSet_UsuarioSet]
GO
ALTER TABLE [dbo].[CancionSet]  WITH CHECK ADD  CONSTRAINT [FK_CancionAlbum] FOREIGN KEY([Album_Id])
REFERENCES [dbo].[AlbumSet] ([Id])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[CancionSet] CHECK CONSTRAINT [FK_CancionAlbum]
GO
ALTER TABLE [dbo].[ListaReproduccionCancion]  WITH CHECK ADD  CONSTRAINT [FK_ListaReproduccionCancion_Cancion] FOREIGN KEY([Cancion_Id])
REFERENCES [dbo].[CancionSet] ([Id])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[ListaReproduccionCancion] CHECK CONSTRAINT [FK_ListaReproduccionCancion_Cancion]
GO
ALTER TABLE [dbo].[ListaReproduccionCancion]  WITH CHECK ADD  CONSTRAINT [FK_ListaReproduccionCancion_ListaReproduccion] FOREIGN KEY([ListaReproduccion_Id])
REFERENCES [dbo].[ListaReproduccionSet] ([Id])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[ListaReproduccionCancion] CHECK CONSTRAINT [FK_ListaReproduccionCancion_ListaReproduccion]
GO
ALTER TABLE [dbo].[UsuarioListaReproduccion]  WITH CHECK ADD  CONSTRAINT [FK_UsuarioListaReproduccion_ListaReproduccion] FOREIGN KEY([ListaReproduccion_Id])
REFERENCES [dbo].[ListaReproduccionSet] ([Id])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[UsuarioListaReproduccion] CHECK CONSTRAINT [FK_UsuarioListaReproduccion_ListaReproduccion]
GO
ALTER TABLE [dbo].[UsuarioListaReproduccion]  WITH CHECK ADD  CONSTRAINT [FK_UsuarioListaReproduccion_Usuario] FOREIGN KEY([Usuario_Id])
REFERENCES [dbo].[UsuarioSet] ([Id])
GO
ALTER TABLE [dbo].[UsuarioListaReproduccion] CHECK CONSTRAINT [FK_UsuarioListaReproduccion_Usuario]
GO
USE [master]
GO
ALTER DATABASE [SiReMu] SET  READ_WRITE 
GO
