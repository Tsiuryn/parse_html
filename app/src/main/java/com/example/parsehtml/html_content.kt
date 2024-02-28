package com.example.parsehtml


//    val html = """
//        <p style="text-align:left;"><span style="color: rgb(255,0,0);font-size: 14px;"><strong>Имеется</strong></span><strong> </strong><span style="color: rgb(67,28,249);font-size: 18px;"><em>активный</em></span> <span style="color: rgb(76,255,63);font-size: 11px;"><strong><em>пакет</em></strong></span> <span style="color: rgb(188,13,212);font-size: 30px;"><strong><em><ins>услуг</ins></em></strong></span></p>
//    """

//    val html = """
//        <p style="text-align:left;"><span style="color: rgb(255,0,0);font-size: 14px;">Имеется</span> <span style="color: rgb(67,28,249);font-size: 18px;"><em>активный</em></span> <span style="color: rgb(76,255,63);font-size: 11px;"><strong><em>пакет</em></strong></span> <span style="color: rgb(188,13,212);font-size: 30px;"><strong><em><ins>услуг</ins></em></strong></span></p>
//<p></p>
//<ol>
//<li><span style="color: #cd1fe6;font-size: 16px;">first</span></li>
//<li><span style="color: #cd1fe6;font-size: 16px;">second</span></li>
//<li><span style="color: #cd1fe6;font-size: 16px;">third</span></li>
//</ol>
//<p></p>
//    """

//    val html = """
//        <p style="text-align:left;"><span style="color: rgb(255,0,0);font-size: 14px;">Имеется</span> <span style="color: rgb(67,28,249);font-size: 18px;"><em>активный</em></span> <span style="color: rgb(76,255,63);font-size: 11px;"><strong><em>пакет</em></strong></span> <span style="color: rgb(188,13,212);font-size: 30px;"><strong><em><ins>услуг</ins></em></strong></span></p>
//<p></p>
//<ul>
//<li><span style="color: rgb(205,31,230);font-size: 18px;">first</span></li>
//<li><span style="color: rgb(205,31,230);font-size: 16px;">second</span></li>
//<li><span style="color: rgb(205,31,230);font-size: 16px;">third</span></li>
//</ul>
//<p></p>
//    """

//    val html = """
//    <p style="text-align:left;"><span style="color: rgb(255,0,0);font-size: 18px;">Имеется</span> <span style="color: rgb(67,28,249);font-size: 18px;"><em>активный</em></span> <span style="color: rgb(76,255,63);font-size: 11px;"><strong><em>пакет</em></strong></span> <span style="color: rgb(188,13,212);font-size: 30px;"><strong><em><ins>услуг</ins></em></strong></span></p>
//<p><a href="https://translate.google.by/?sl=ru&tl=en&text=%D0%BA%D0%B0%D1%80%D1%82%D0%B0%20%D0%BD%D0%B5%D0%BE%D0%B1%D1%80%D0%B0%D0%B1%D0%B0%D1%82%D1%8B%D0%B2%D0%B0%D0%B5%D0%BC%D1%8B%D1%85%20%D1%82%D1%8D%D0%B3%D0%BE%D0%B2&op=translate" target="_blank"><span style="color: rgb(205,31,230);font-size: 16px;">translator</span></a>&nbsp;</p>
//<p></p>
//    """
//
//    val html = """
//        <p style="text-align:right;"><span style="color: rgb(255,0,0);font-size: 14px;">Имеется</span> <span style="color: rgb(67,28,249);font-size: 18px;"><em>активный</em></span> <span style="color: rgb(76,255,63);font-size: 11px;"><strong><em>пакет</em></strong></span> <span style="color: rgb(188,13,212);font-size: 30px;"><strong><em><ins>услуг</ins></em></strong></span></p>
//    """

//    val html = """
//        <p><span style="color: rgb(47,241,50);">Редактор </span><span style="color: #f12f5a;"><strong>форматированного</strong></span><span style="color: rgb(47,241,50);"> текста</span></p>
//    """

//    val html = """
//        "<p style=\"text-align:center;\"><span style=\"color: rgb(247,2,2);font-size: 11px;\">Text example</span><span style=\"color: rgb(247,2,2);font-size: 30px;\"> </span><a href=\"https://www.google.com/\" target=\"_blank\"><span style=\"color: #3b29ec;font-size: 30px;\">google</span></a><span style=\"color: #3b29ec;font-size: 30px;\"> </span></p>\n"
//    """

// Ссылки
val html = """
        <p style="text-align:center;"><span style="color: rgb(247,2,2);font-size: 11px;">Text example </span><a href="https://www.google.com/" target="_blank"><span style="color: rgb(247,2,2);font-size: 16px;">google</span></a><span style="color: rgb(247,2,2);font-size: 11px;"> </span></p>\n
    """

        // Списки
//        val html = """
//        <p style=\"text-align:left;\">Пример списков:</p>\n<ul>\n<li>1 номер</li>\n<li>2 номер</li>\n<li>3 номер</li>\n</ul>\n
//    """
//        val html = """
//        <p style=\"text-align:right;\">Пример списков:</p>
//        <ol>
//        <li style=\"text-align:left;\">1 номер</li>
//        <li style=\"text-align:center;\">2 номер</li>
//        <li style=\"text-align:right;\">3 номер</li>
//        </ol>
//        <p></p>
//    """
//        val html = """
//        <p style=\"text-align:left;\">Пример списков <a href=\"https://yandex.com/\" target=\"_blank\">ываывава</a> :</p>\n<ol>\n<li style=\"text-align:left;\">1 номер</li>\n<li style=\"text-align:left;\">2 номер</li>\n<li style=\"text-align:left;\">3 номер</li>\n</ol>\n<p></p>\n
//    """