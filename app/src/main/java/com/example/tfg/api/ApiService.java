package com.example.tfg.api;

import com.example.tfg.entities.ApelacionGuardia;
import com.example.tfg.entities.Guardia;
import com.example.tfg.entities.RecursoExtraOrdinario;
import com.example.tfg.entities.RecursoGuardia;
import com.example.tfg.entities.Registro;
import com.example.tfg.entities.SituacionGuardia;
import com.example.tfg.entities.Usuario;

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

    @PATCH("registro/{id}/situacion1")
    Call<Void> updateSituacion1(
            @Path("id") Long id,
            @Query("presentado") boolean presentado,
            @Query("validado") boolean validado,
            @Query("pagado") boolean pagado,
            @Query("nTalon") Integer nTalon,
            @Query("comentarios") String comentarios);

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


    // SITUACION GUARDIA

    @GET("situaciones-guardia/guardia/{guardiaId}")
    Call<SituacionGuardia> getByGuardiaId(@Path("guardiaId") Long guardiaId);

    @PUT("situaciones-guardia/{id}")
    Call<SituacionGuardia> updateSituacionGuardia(@Path("id") Long id, @Body SituacionGuardia situacionGuardia);

    @POST("situaciones-guardia")
    Call<SituacionGuardia> createSituacionGuardia(@Body SituacionGuardia situacion);



    // APELACIONES GUARDIA

    @GET("apelaciones-guardia/guardia/{guardiaId}")
    Call<List<ApelacionGuardia>> getApelacionByGuardiaId(@Path("guardiaId") Long guardiaId);

    @POST("apelaciones-guardia")
    Call<ApelacionGuardia> createApelacion(@Body ApelacionGuardia apelacion);

    @PUT("apelaciones-guardia/{id}")
    Call<ApelacionGuardia> updateApelacion(@Path("id") Long id, @Body ApelacionGuardia apelacion);


    // RECURSO GUARDIA
    @GET("recursos-guardia/guardia/{guardiaId}")
    Call<RecursoGuardia> getRecursoGuardiaByGuardiaId(@Path("guardiaId") Long guardiaId);

    @POST("recursos-guardia")
    Call<RecursoGuardia> createRecursoGuardia(@Body RecursoGuardia recurso);

    @PUT("recursos-guardia/{id}")
    Call<RecursoGuardia> updateRecursoGuardia(@Path("id") Long id, @Body RecursoGuardia recurso);


    // RECURSO EXTRAORDINARIO
    @GET("recursos-extraordinarios/guardia/{guardiaId}")
    Call<RecursoExtraOrdinario> getRecursoExtraByGuardiaId(@Path("guardiaId") Long guardiaId);

    @POST("recursos-extraordinarios")
    Call<RecursoExtraOrdinario> createRecursoExtra(@Body RecursoExtraOrdinario recurso);

    @PUT("recursos-extraordinarios/{id}")
    Call<RecursoExtraOrdinario> updateRecursoExtra(@Path("id") Long id, @Body RecursoExtraOrdinario recurso);


}