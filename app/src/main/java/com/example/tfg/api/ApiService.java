package com.example.tfg.api;

import com.example.tfg.Guardia;
import com.example.tfg.Registro;
import com.example.tfg.Usuario;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    // REGISTRO

    @GET("registro/registros")
    Call<List<Registro>> getAllRegistros();

    @GET("registro/registro/{id}")
    Call<Registro> getRegistroById(@Path("id") Long id);

    @POST("registro/registro")
    Call<Registro> createRegistro(@Body Registro registro);

    @PUT("registro/actualizarRegistro/{id}")
    Call<Registro> updateRegistro(@Path("id") Long id, @Body Registro registro);

    @DELETE("registro/borrarRegistro/{id}")
    Call<Void> deleteRegistro(@Path("id") Long id);

    // USUARIO

    @GET("usuario/usuarios")
    Call<List<Usuario>> getTodosLosUsuarios();

    @GET("usuario/{id}")
    Call<Usuario> getUsuarioPorId(@Path("id") long id);

    @GET("usuario/username/{username}")
    Call<Usuario> getUsuarioPorUsername(@Path("username") String username);

    @POST("usuario")
    Call<Usuario> crearUsuario(@Body Usuario nuevoUsuario);

    @PUT("usuario/actualizarUsuario/{id}")
    Call<Usuario> actualizarUsuario(@Path("id") long id, @Body Usuario usuarioActualizado);

    @DELETE("usuario/borrarUsuario/{id}")
    Call<Map<String, String>> eliminarUsuario(@Path("id") long id);

    // GUARDIAS

    @GET("guardias")
    Call<List<Guardia>> getAllGuardias();

    @GET("guardias/{id}")
    Call<Guardia> getGuardiaById(@Path("id") Long id);

    @POST("guardias")
    Call<Guardia> createGuardia(@Body Guardia guardia);

    @PUT("guardias/{id}")
    Call<Guardia> updateGuardia(@Path("id") Long id, @Body Guardia guardia);

    @DELETE("guardias/{id}")
    Call<Void> deleteGuardia(@Path("id") Long id);

    @GET("guardias/por-juzgado/{porJuzgado}")
    Call<List<Guardia>> getGuardiasPorJuzgado(@Path("porJuzgado") boolean porJuzgado);

    @GET("guardias/por-nombre/{nombre}")
    Call<List<Guardia>> getGuardiasPorNombre(@Path("nombre") String nombre);

    @GET("guardias/por-cobrado/{cobrado}")
    Call<List<Guardia>> getGuardiasPorCobrado(@Path("cobrado") boolean cobrado);
}