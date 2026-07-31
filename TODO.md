## Task: Fix PostgreSQL "could not determine data type of parameter $5" error

### Steps:
1. [x] Create `AuditoriaSpecification.java` using JPA Criteria API
2. [x] Create `MovimientoInventarioSpecification.java` using JPA Criteria API
3. [x] Edit `AuditoriaRepository.java` to extend `JpaSpecificationExecutor<Auditoria>`
4. [x] Edit `MovimientoInventarioRepository.java` to extend `JpaSpecificationExecutor<MovimientoInventario>`
5. [x] Edit `ReporteExamenService.java` to use Specifications instead of JPQL queries
6. [x] Rebuild and verify the fix

