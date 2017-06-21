# Web scraper dla polskich aktów prawnych

## Lista dostępny dokumentów

 * [ ] Dziennik Ustaw
 * [ ] Monitor Polski
 * [ ] Kodeksy
 * [ ] Orzeczenia
   * ONSA i WSA
   * OSNC
   * OSNKW
   * OSNP
   * OTK-A
   * OSA
   * Przegląd Orzecznictwa Podatkowego  
 * [ ] Analizy orzeczeń
   * Europejski Przegląd Sądowy
   * Finanse Komunalne
   * Gdańskie Studia Prawnicze - Przegląd Orzecznictwa
   * Glosa
   * Państwo i Prawo
   * Prokurator
   * Prokuratura i Prawo
   * Przegląd Podatkowy
   * Przegląd Prawa Handlowego
   * Samorząd Terytorialny 
   
   
## Jak to działa

### Pobranie danych
 * Pobranie informacji o dostępnych zasobach i zapisanie jej w pliku resource.csv
 * Pobranie zasobów (np. plików pdf)
 * Zamiana pliku pdf na tekst lub obrazek (niektóre są skanami)
 * OCR na obrazkach by otrzymać tekst

### Tworzenie bazy wiedzy
 * Utworzenie indeksu
 
 
## Zewnętrzne programy
 
 * OCR - sudo apt-get install tesseract-ocr-pol