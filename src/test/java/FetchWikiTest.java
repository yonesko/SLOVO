import data.dao.FetchWiki;
import data.model.WordInfo;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by gleb on 04.04.16.
 */
public class FetchWikiTest {

    @Test
    public void findWord() throws Exception {
        findWord("поношение", "WordInfo{name='поношение', meaning='1. оскорбление ◆ Вот почему следует забыть поношение, нанесенное ему\n" +
                "    Всеволодом Ивановым, который, желая выразить степень своего преклонения\n" +
                "    перед ушедшим писателем, напечатал (несомненно, против своего сердца) во\n" +
                "    «Встречах с Горьким»: «Россия дала ему всю силу любви, как она дает ее\n" +
                "    сегодня Сталину». Ю. П. Анненков, «Дневник моих встреч», 1966 г. (цитата из\n" +
                "    Национального корпуса русского языка, см. Список литературы)\n" +
                " 2. позор, бесчестье \n" +
                "', etymology='null'}");

        findWord("железка", "WordInfo{name='железка', meaning='1. разг. металлическая вещь ◆ Не указан пример употребления (см. рекомендации\n" +
                "    ).\n" +
                " 2. разг. то же, что железная дорога \n" +
                "', etymology='Суффиксное производное от существительного железо, далее от праслав. *žel-zo,\n" +
                "от кот. в числе прочего произошли: др.-русск., ст.-слав. жєлѣзо (др.-греч.\n" +
                "σίδηρος), желѣзнъ (σιδηροῦς), русск. железо (диал. зеле́зо, зяле́зо), укр.\n" +
                "залíзо, желíзо, белор. зеле́зо, зале́зо, болг. желя́зо, сербохорв. жѐљезо,\n" +
                "словенск. želézo, чешск., словацк. železo, польск. żelazo, в.-луж., н.-луж.\n" +
                "železo. Родственно лит. geležìs, жем. gelžìs, латышск. dzèlzs, далее — греч.\n" +
                "гомер. χαλκός «медь, бронза». Сюда же относили название занимающихся ковкой\n" +
                "меди Τελχῖνες, Θελγῖνες. Праслав. *žel-zo, скорее всего, родственно ст.-слав.\n" +
                "желы «черепаха», греч. χέλυς — то же, русск. желва́к, голова́, польск. głaz\n" +
                "«камень» с общим исходным знач. «камень».Использованы данные словаря М. Фасмера\n" +
                "  с комментариями О. Н. Трубачёва; см. Список литературы.\n" +
                "'}");
        findWord("эксплуатант", "WordInfo{name='эксплуатант', meaning='1. тот, кто осуществляет эксплуатацию орудий и средств производства,\n" +
                "    механизмов, сооружений, зданий, территории и т. п. \n" +
                " 2.\n" +
                "', etymology='null'}");
        findWord("тибетский", "WordInfo{name='тибетский', meaning='1. относящийся к Тибету, тибетцам, связанный с ними \n" +
                " 2. свойственный тибетцам, характерный для них и для Тибета \n" +
                " 3. принадлежащий Тибету, тибетцам \n" +
                " 4. созданный, выведенный и т. п. на Тибете или тибетцами \n" +
                " 5. субстантивир., неисч., разг. то же, что тибетский язык; язык\n" +
                "    тибето-бирманской ветви китайско-тибетской группы \n" +
                "', etymology='null'}");
        findWord("воспроизводимый", null);
    }
    private void findWord(String word, String expected) {
        String actual = null;
        if (expected != null)
            actual = FetchWiki.findWord(word).toString();
        Assert.assertEquals(expected, actual);
    }
}