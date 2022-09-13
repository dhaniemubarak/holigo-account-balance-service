package id.holigo.services.holigoaccountbalanceservice.repositories;

import id.holigo.services.holigoaccountbalanceservice.domain.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    Language findByMessageKeyAndLocale(String messageKey, String locale);

}
