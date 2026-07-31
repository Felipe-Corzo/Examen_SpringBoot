package com.logitrack.repository;

import com.logitrack.model.Auditoria;
import com.logitrack.model.TipoOperacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface AuditoriaRepository extends JpaRepository<Auditoria, Long>,
                                             JpaSpecificationExecutor<Auditoria> {

    List<Auditoria> findByEntidadAfectadaIgnoreCase(String entidadAfectada);

    List<Auditoria> findByTipoOperacion(TipoOperacion tipoOperacion);

    List<Auditoria> findByUsuario_Id(Long usuarioId);

    List<Auditoria> findAllByOrderByFechaHoraDesc();
}
