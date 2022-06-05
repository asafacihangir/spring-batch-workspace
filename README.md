# Spring Batch Practice Example 1


Bu çalışmada ./public/upload dosya yoluna yüklenen .json uzantılı dosyalar anlık olarak işleniyor. 


## Test Senaryo
- Uygulama çalıştırıldıkdan sonra ./data/persons.json adresinde bulunan dosya ./public/upload adresine kopyalanır.
- ./data/persons.json adresinde bulunan dosya okunur, çeşitli kontrollerden geçtikten sonra, dosyanın işlenmiş verisi ./public/completed adresine taşınır.

