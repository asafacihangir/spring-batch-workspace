# Item Processor

- Standard interface for interjecting custom business logic
- May transform, filter, and validate items
- Chaining of multiple processors via CompositeltemProcessor 
- Optional component

```
public interface ItemProcessor<I, O> {
	O process(@NonNull I item) throws Exception;
}
```

# PreRun Installations
-  ./data/SHIPPED_ORDER.sql scritini çalıştırınız.

# Demo
- Bu çalışmada;
  * SHIPPED_ORDER tablosundan veriler alınıyor.(Item Reader)
  * Alınan Order verileri TrackedOrder verisine dönüştürülüyor.(Item Processor)
  * Oluşturulan TrackedOrder verileri ./data/shipped_orders_output.json dosyasına yazılıyor.

