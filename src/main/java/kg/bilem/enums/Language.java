package kg.bilem.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public enum Language {
    RUSSIAN("Русский"), KYRGYZ("Кыргызский");

    private final String language;

    public static Language of(String language) {
        return Stream.of(Language.values())
                .filter(l -> l.getLanguage().equals(language))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
