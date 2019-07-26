%% LaTeX2e class for student assignments
%% sdqassignment.cls
%%
%% Karlsruhe Institute of Technology
%% Institute for Program Structures and Data Organization
%% Chair for Software Design and Quality (SDQ)
%%
%% Yves R. Schneider
%% yves.schneider@kit.edu
%%
%% 2019/04/15 version V1.0
\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{sdqassignment}[2019/04/15 V1.0 SDQ Assignment Class]
\LoadClass[12pt,a4paper,oneside]{scrartcl}

%% --------------
%% | Typography |
%% --------------
\RequirePackage[T1]{fontenc}
\RequirePackage[utf8]{inputenc}
\RequirePackage[ngerman]{babel}
\RequirePackage{lmodern}
\RequirePackage{textcomp}
\RequirePackage[protrusion=true,expansion=true]{microtype}
\RequirePackage[all]{nowidow}

%% -----------
%% | Graphic |
%% -----------
\RequirePackage[pdftex]{graphicx}
    \graphicspath{{./graphics/}}
    \DeclareGraphicsExtensions{.pdf,.eps,.png,.jpeg}
\RequirePackage{xcolor}
    \definecolor{kitblack}{RGB}{0,0,0}
    \definecolor{kitgreen}{RGB}{0,150,130}
    \definecolor{kitblue}{RGB}{70,100,170}
    \definecolor{kitpeagreen}{RGB}{140,182,60}
    \definecolor{kityellow}{RGB}{252,229,0}
    \definecolor{kitorange}{RGB}{223,155,27}
    \definecolor{kitbrown}{RGB}{167,130,46}
    \definecolor{kitred}{RGB}{162,34,35}
    \definecolor{kitpurple}{RGB}{163,16,124}
    \definecolor{kitcyanblue}{RGB}{35,161,224}
\RequirePackage{tikz}
\RequirePackage{fontawesome}
\RequirePackage{tcolorbox}
    \newtcolorbox{questionbox}{colback=kitorange!15,colframe=kitorange,sharp corners=downhill,fonttitle=\bfseries\large,title=\faQuestionCircle~Frage}
    \newtcolorbox{informationbox}{colback=kitpeagreen!15,colframe=kitpeagreen,sharp corners=downhill,fonttitle=\bfseries\large,title=\faInfoCircle~Information}
    \newtcolorbox{examplebox}{colback=kitblue!15,colframe=kitblue,sharp corners=downhill,fonttitle=\bfseries\large,title=\faExclamationCircle~Beispiel}
    \newtcolorbox{sequencebox}{colback=kitblack!15,colframe=kitblack,sharp corners=downhill,fonttitle=\bfseries\large,title=\faChevronCircleRight~Beispielablauf}
    \newtcolorbox{checkstylebox}{colback=kitred!15,colframe=kitred,sharp corners=downhill,fonttitle=\bfseries\large,title=\faCheckCircle~Checkstyle}
    \newtcolorbox{terminalbox}{colback=kitpurple!15,colframe=kitpurple,sharp corners=downhill,fonttitle=\bfseries\large,title=\faTerminal~Terminal-Klasse}

%% ----------------
%% | Page setting |
%% ----------------
\RequirePackage{scrlayer-scrpage}
\RequirePackage{lastpage}
\RequirePackage{stfloats}
\RequirePackage{adjustbox}

%% ---------
%% | Table |
%% ---------
\RequirePackage{multirow}
\RequirePackage{booktabs}
\RequirePackage{array}

%% --------
%% | Math |
%% --------
\RequirePackage{amsmath}
\RequirePackage[cmintegrals]{newtxmath}
\RequirePackage{bm}
\RequirePackage{amssymb}
\RequirePackage{algorithmic}

%% ---------
%% | Other |
%% ---------
\RequirePackage{xargs}
\RequirePackage{ifthen}
\RequirePackage{csquotes}
\RequirePackage{enumitem}
\RequirePackage{todonotes}
\RequirePackage{footmisc}

% --------
% | Diff |
% --------
\RequirePackage[normalem]{ulem}
    \newcommand{\DIFadd}[1]{{\protect\color{kitblue}\uwave{#1}}}
    \newcommand{\DIFdel}[1]{{\protect\color{kitred}\sout{#1}}}

%% --------
%% | Code |
%% --------
\RequirePackage{listings}
\lstset{inputpath=./listings/,literate=
  {á}{{\'a}}1 {é}{{\'e}}1 {í}{{\'i}}1 {ó}{{\'o}}1 {ú}{{\'u}}1
  {Á}{{\'A}}1 {É}{{\'E}}1 {Í}{{\'I}}1 {Ó}{{\'O}}1 {Ú}{{\'U}}1
  {à}{{\`a}}1 {è}{{\`e}}1 {ì}{{\`i}}1 {ò}{{\`o}}1 {ù}{{\`u}}1
  {À}{{\`A}}1 {È}{{\'E}}1 {Ì}{{\`I}}1 {Ò}{{\`O}}1 {Ù}{{\`U}}1
  {ä}{{\"a}}1 {ë}{{\"e}}1 {ï}{{\"i}}1 {ö}{{\"o}}1 {ü}{{\"u}}1
  {Ä}{{\"A}}1 {Ë}{{\"E}}1 {Ï}{{\"I}}1 {Ö}{{\"O}}1 {Ü}{{\"U}}1
  {â}{{\^a}}1 {ê}{{\^e}}1 {î}{{\^i}}1 {ô}{{\^o}}1 {û}{{\^u}}1
  {Â}{{\^A}}1 {Ê}{{\^E}}1 {Î}{{\^I}}1 {Ô}{{\^O}}1 {Û}{{\^U}}1
  {œ}{{\oe}}1 {Œ}{{\OE}}1 {æ}{{\ae}}1 {Æ}{{\AE}}1 {ß}{{\ss}}1
  {ű}{{\H{u}}}1 {Ű}{{\H{U}}}1 {ő}{{\H{o}}}1 {Ő}{{\H{O}}}1
  {ç}{{\c c}}1 {Ç}{{\c C}}1 {ø}{{\o}}1 {å}{{\r a}}1 {Å}{{\r A}}1
  {€}{{\EUR}}1 {£}{{\pounds}}1
}

% --------
% | Java |
% --------
\lstloadlanguages{Java}
\lstdefinestyle{java}{
    language=Java,
    numbers=left,
    captionpos=b,
    basicstyle=\small \ttfamily,
    keywordstyle=\color{kitpurple}\bfseries,
    stringstyle=\color{kitred},
    commentstyle=\color{kitgreen}\emph,
    morecomment=[s][\color{kitblue}]{/**}{*/},
    columns=flexible,
    frame=shadowbox,
    tabsize=4,
    mathescape=true,
    escapechar=§,
    showspaces=false,
    showtabs=false,
    showstringspaces=false,
    breaklines=true,
    breakatwhitespace=true,
}

\newcommand{\lstinputjava}[1]{\begin{center}\begin{minipage}[c]{0.91\linewidth}\centering\lstinputlisting[style=java]{#1.java}\end{minipage}\end{center}}

\newenvironment{lstlistingjava}{\begin{center}\begin{minipage}[c]{0.91\linewidth}\centering\begin{lstlisting}[style=java]}{\end{lstlisting}\end{minipage}\end{center}}

\newcommand{\lstinlinejava}[1]{\lstinline[style=java, basicstyle=\normalsize \ttfamily, columns=fixed]{#1}}

% --------
% | Text |
% --------
\lstdefinestyle{txt}{
    numbers=left,
    captionpos=b,
    basicstyle=\small \ttfamily,
    columns=flexible,
    frame=shadowbox,
    tabsize=4,
    mathescape=true,
    escapechar=§,
    showspaces=false,
    showtabs=false,
    showstringspaces=false,
    breaklines=true,
    breakatwhitespace=true,
}

\newcommand{\lstinputtxt}[1]{\begin{center}\begin{minipage}[c]{0.91\linewidth}\centering\lstinputlisting[style=txt]{#1.txt}\end{minipage}\end{center}}

\newenvironment{lstlistingtxt}{\begin{center}\begin{minipage}[c]{0.91\linewidth}\centering\begin{lstlisting}[style=txt]}{\end{lstlisting}\end{minipage}\end{center}}

\newcommand{\lstinlinetxt}[1]{\lstinline[style=txt, basicstyle=\normalsize \ttfamily, columns=fixed]{#1}}

% ------------
% | Sequence |
% ------------
\lstdefinestyle{sequence}{
    numbers=left,
    numbersep=20pt,
    captionpos=b,
    basicstyle=\small \ttfamily,
    columns=flexible,
    frame=leftline,
    framesep=10pt,
    tabsize=4,
    mathescape=true,
    escapechar=§,
    showspaces=false,
    showtabs=false,
    showstringspaces=false,
    breaklines=true,
    breakatwhitespace=true,
}

\newcommand{\lstinputsequence}[1]{\begin{sequencebox}\vspace{-10pt}\begin{flushright}\begin{minipage}[r]{0.95\linewidth}\raggedleft\lstinputlisting[style=sequence]{#1.txt}\end{minipage}\end{flushright}\vspace{-24pt}\end{sequencebox}}

\newenvironment{lstlistingsequence}{\begin{sequencebox}\vspace{-10pt}\begin{flushright}\begin{minipage}[r]{0.95\linewidth}\raggedleft\begin{lstlisting}[style=sequence]}{\end{lstlisting}\end{minipage}\end{flushright}\vspace{-24pt}\end{sequencebox}}

\newcommand{\lstinlinesequence}[1]{\lstinline[style=sequence, basicstyle=\normalsize \ttfamily, columns=fixed]{#1}}

%% ----------------
%% | Ref­er­enc­ing  |
%% ----------------
\RequirePackage[T1,hyphens]{url}
\RequirePackage[breaklinks,colorlinks=false]{hyperref}
\RequirePackage[all]{hypcap}
\RequirePackage[capitalise,nameinlink]{cleveref}
\RequirePackage{xspace}
\RequirePackage{scrhack}


%% -------------------------------------------
%% | Variables for headers, footers and title|
%% -------------------------------------------
\newcommand{\thelecture}{}
\newcommand{\thesemester}{}
\newcommand{\thegroup}{}
\newcommand{\thelecturer}{}
\newcommand{\themail}{}
\newcommand{\theilias}{}
\newcommand{\theassignment}{}
\newcommand{\theversion}{}
\newcommand{\thepoints}{}
\newcommand{\thebonus}{}
\newcommand{\thereleasedate}{}
\newcommand{\thepraktomatdate}{}
\newcommand{\theduedate}{}

\newcommand{\lecture}[1]{\renewcommand{\thelecture}{#1}}
\newcommand{\semester}[1]{\renewcommand{\thesemester}{#1}}
\newcommand{\group}[1]{\renewcommand{\thegroup}{#1}}
\newcommand{\lecturer}[1]{\renewcommand{\thelecturer}{#1}}
\newcommand{\mail}[1]{\renewcommand{\themail}{#1}}
\newcommand{\ilias}[1]{\renewcommand{\theilias}{#1}}
\newcommand{\assignment}[1]{\renewcommand{\theassignment}{#1}}
\newcommand{\version}[1]{\renewcommand{\theversion}{#1}}
\newcommand{\points}[1]{\renewcommand{\thepoints}{#1}}
\newcommand{\bonus}[1]{\renewcommand{\thebonus}{#1}}
\newcommand{\releasedate}[1]{\renewcommand{\thereleasedate}{#1}}
\newcommand{\duedate}[1]{\renewcommand{\theduedate}{#1}}
\newcommand{\praktomatdate}[1]{\renewcommand{\thepraktomatdate}{#1}}

%% ---------------------
%% | Header and footer |
%% ---------------------
\renewcommand{\thesection}{\Alph{section}}
\newcommand{\task}[2]{\ifthenelse{#2 = 1}{\section{#1 (#2 Punkt)}}{\section{#1 (#2 Punkte)}}}
\newcommand{\clause}[1]{\section*{#1}}
\ihead*{\thelecture~--~\theassignment}
\ohead*{Abgabefrist:~\theduedate}
\cfoot*{Seite~\thepage{}~von~\pageref{LastPage}}
\setheadsepline{.5pt}
\setfootsepline{.5pt}

%% ---------
%% | Title |
%% ---------
\AtBeginDocument{
\hypersetup{
  pdftitle=\theassignment,
  pdfauthor=\thegroup,
  pdfsubject=\thelecture
}
\thispagestyle{empty}
\vspace*{-10ex}
\begin{adjustbox}{center}
\begin{tabular}{>{\centering\arraybackslash}p{0.38\linewidth}>{\centering\arraybackslash}p{0.18\linewidth}>{\centering\arraybackslash}p{0.44\linewidth}}
\begin{minipage}[c]{0.86\linewidth}
    \includegraphics[width=\linewidth]{kit_logo_de_4c_positiv.pdf}
\end{minipage} & \multicolumn{2}{r}{\begin{tabular}[l]{@{}l@{}}
    \\
    \\
    \thelecturer \\
    \thegroup \\
    \small{\url{\theilias}} \\
    \small{\href{mailto:\themail}{\nolinkurl{\themail}}} \\
    \\
    \\
\end{tabular}
} \\ \hline \\
    \multicolumn{3}{c}{\textbf{\LARGE \thelecture{ }-- \thesemester}} \\
    \\
\begin{tabular}[c]{@{}c@{}}
    \\
    \textbf{\LARGE \theassignment}\\
    \theversion\\
\end{tabular} & \begin{tabular}[c]{@{}c@{}}
    \\
    \thepoints\\
    \thebonus\\
\end{tabular} & \begin{tabular}[c]{@{}ll@{}}
    Ausgabe: & \thereleasedate \\
    Praktomat: & \thepraktomatdate \\
    Abgabefrist: & \theduedate \\
\end{tabular} \\ \hline
\end{tabular} \\
\end{adjustbox}
}

%% ------------
%% | Commands |
%% ------------
\newcommand{\Java}{\textsc{Java}{}}
\newcommand{\x}{$\times$}

%% ------------
%% | Hinweise |
%% ------------
\newcommand{\abgabehinweise}[2]{
    \clause{Abgabehinweise}
    Bitte beachten Sie, dass das erfolgreiche Bestehen der öffentlichen Tests für eine erfolgreiche Abgabe dieses Blattes notwendig ist. Der Praktomat wird Ihre Abgabe zurückweisen, falls eine der nachfolgenden Regeln verletzt ist. Eine zurückgewiesene Abgabe wird automatisch mit null Punkten bewertet. Planen Sie entsprechend Zeit für Ihren ersten Abgabeversuch ein.
    \ifthenelse{#1 > 0}{\begin{itemize}
        \ifthenelse{#1 > 0}{\item Achten Sie auf fehlerfrei kompilierenden Programmcode.}{}
        \ifthenelse{#1 > 0}{\item Verwenden Sie keine Klassen der Java-Bibliotheken, ausgenommen Klassen der Pakete #2.}{}
        \ifthenelse{#1 > 1}{\item Achten Sie darauf, nicht zu lange Zeilen, Methoden und Dateien zu erstellen. Sie müssen bei Ihren Lösungen eine maximale Zeilenbreite von 120 Zeichen einhalten.}{}
        \ifthenelse{#1 > 1}{\item Halten Sie alle Whitespace-Regeln ein.}{}
        \ifthenelse{#1 > 1}{\item Halten Sie alle Regeln zu Variablen-, Methoden- und Paketbenennung ein.}{}
        \ifthenelse{#1 > 2}{\item Wählen Sie geeignete Sichtbarkeiten für Ihre Klassen, Methoden und Attribute.}{}
        \ifthenelse{#1 > 3}{\item 4}{}
        \ifthenelse{#1 > 4}{\item 5}{}
    \end{itemize}}{}
}

\newcommand{\bearbeitungshinweise}[1]{
    \clause{Bearbeitungshinweise}
    Diese Bearbeitungshinweise sind relevant für die Bewertung Ihrere Abgabe, jedoch wird der Praktomat Ihre Abgabe \textbf{nicht} zurückweisen, falls einer der nachfolgenden Regeln verletzt ist.
    \ifthenelse{#1 > 0}{\begin{itemize}
        \ifthenelse{#1 > 0}{\item Achten Sie darauf, dass Ihre Abgaben sowohl in Bezug auf objektorientierte Modellierung, als auch Funktionalität bewertet werden.}{}
        \ifthenelse{#1 > 0}{\item Programmcode muss in englischer Sprache verfasst sein.}{}
        \ifthenelse{#1 > 0}{\item Kommentieren Sie Ihren Code angemessen: So viel wie nötig, so wenig wie möglich.}{}
        \ifthenelse{#1 > 0}{\item Die Kommentare sollen einheitlich in englischer oder deutscher Sprache verfasst werden.}{}
        \ifthenelse{#1 > 1}{\item Wählen Sie aussagekräftige Namen für alle Ihre Bezeichner.}{}
        \ifthenelse{#1 > 3}{\item 4}{}
        \ifthenelse{#1 > 4}{\item 5}{}
    \end{itemize}}{}
}


\newcommandx{\hinweise}[2][2={\lstinlinejava{java.lang} und \lstinlinejava{java.io}}]{
    \abgabehinweise{#1}{#2}
    \ifthenelse{#1 > 1}{\checkstyleinfo}{}
    \bearbeitungshinweise{#1}
}

\newcommand{\allgemein}{

}

\newcommand{\checkstyleinfo}{
\begin{checkstylebox}
    Beginnend mit diesem Übungsblatt überprüft der Praktomat Ihre Quelltexte während der Abgabe automatisiert auf die Einhaltung obenstehender Regeln. Falls eine Regel entsprechend markiert ist, wird der Praktomat die Abgabe zurückweisen, wenn die Regel nicht eingehalten wird. Andere Regelverletzungen können zu Punktabzug führen. Sie sollten Ihre Quelltexte bereits während der Entwicklung auf die Regeleinhaltung überprüfen. Das Programmieren-Wiki (ILIAS) beschreibt, wie dies funktioniert.
\end{checkstylebox}
}

\newcommand{\terminalinfo}{
\begin{terminalbox}
    Laden Sie für diese Aufgabe die Terminal-Klasse herunter und platzieren Sie diese unbedingt im Paket \lstinlinejava{edu.kit.informatik}. Die Methode \lstinlinejava{Terminal.readLine()} liest eine Benutzereingabe von der Konsole und ersetzt \lstinlinejava{System.in}. Die Methode \lstinlinejava{Terminal.printLine()} schreibt eine Ausgabe auf die Konsole und ersetzt \lstinlinejava{System.out}. Verwenden Sie für jegliche Konsoleneingabe oder Konsolenausgabe die Terminal-Klasse. Verwenden Sie in keinem Fall \lstinlinejava{System.in} oder \lstinlinejava{System.out}. Fehlermeldungen werden ausschließlich über die Terminal-Klasse ausgegeben und müssen aus technischen Gründen unbedingt mit \lstinlinetxt{Error,} beginnen.
Laden Sie die Terminal-Klasse \textbf{niemals} zusammen mit Ihrer Abgabe hoch.
\end{terminalbox}
}

\newcommand{\hinweisesommer}{

    \clause{Allgemeine Hinweise}
    Bei organisatorischen Fragen zu ihrem Studiengang und zu speziellen Prüfungssituationen wenden Sie sich bitte an den entsprechenden Studiengangservice. Für die Fakultät für Informatik ist dies beispielsweise der ISS\footnote{\url{https://www.informatik.kit.edu/iss.php}}. Verbindliche Informationen zu den Prüfungsmodalitäten für Programmieren im Rahmen Ihres Studiums erhalten Sie in den jeweils zutreffenden Modulhandbüchern, der Studienordnung und per Nachfrage beim Studiengangservice.

    \clause{Hinweise zum Übungsbetrieb}
    \subsection*{Organisatorisches}
    \begin{itemize}

        \item Im Semesterverlauf werden \textbf{5 Übungsblätter} ausgegeben. Diese erscheinen im Abstand von zwei Wochen. Die Bearbeitungszeit beträgt jeweils zwei Wochen.

        \item Jedes Übungsblatt hat einen Umfang von 20 Punkten. Insgesamt können 100 Punkte durch die Bearbeitung der Übungsblätter erreicht werden. Sie bestehen den Übungsschein durch das Erhalten von mehr als 50\% der maximal zu erreichenden Punktzahl aus den Übungsblättern und mehr als 75\% der maximal zu erreichenden Punktzahl aus der Präsenzübung.

        \item Des Weiteren ist zum Erhalt eines Übungsscheins eine Anmeldung im Campus Management Portal\footnote{\url{https://campus.studium.kit.edu/}} erforderlich. Die \textbf{Anmeldefrist endet am 23.05.2019 um 12 Uhr}. Eine zeitnahe Anmeldung ist jedoch empfohlen.

        \item Der Übungsschein ist unbenotet.

        \item Abgegebene Übungsblätter werden durch einen Tutor korrigiert und bewertet.

        \item In diesem Semester finden \textbf{Präsenztutorien} statt. Alle organisatorischen Informationen werden im ILIAS bekanntgegeben.

        \item Überprüfen Sie das Postfach Ihrer \textbf{KIT-Mailadresse} regelmäßig auf neue E-Mails. Sie erhalten eine Zusammenfassung der Korrektur per E-Mail an diese Adresse. Alle Anmerkungen des Tutors können Sie anschließend im Praktomaten online einsehen.

        \item Der Erhalt des (unbenoteten) Übungsscheins ist Voraussetzung für die Teilnahme an den beiden (benoteten) Abschlussaufgaben.

        \item Die Prüfung Programmieren ist eine \textbf{Orientierungsprüfung}, das heißt es gelten besondere Prüfungsmodalitäten. Insbesondere müssen die beiden Abschlussaufgaben vor Ende des dritten Semesters bestanden werden. Außerdem muss der erste Versuch die Abschlussaufgaben erfolgreich zu bearbeiten vor Ablauf des zweiten Semesters stattgefunden haben.
    \end{itemize}

    \subsection*{Abgabesystem}
    \begin{itemize}

        \item Die Übungsblätter werden über das \textbf{Praktomat-System}\footnote{\url{https://praktomat.cs.kit.edu/}} abgegeben. Abgaben auf anderem Weg werden grundsätzlich abgelehnt.

        \item Die \textbf{Freischaltung der Abgabemöglichkeit} im Praktomat erfolgt jeweils eine Woche nach Ausgabe eines Übungsblattes bzw. eine Woche vor Ablauf der Abgabefrist. Eine frühere Abgabe ist nicht möglich.

        \item Der Praktomat erzwingt eine Mindestqualität der Abgabe, indem Abgaben abgelehnt werden, die bestimmte \textbf{Qualitätskriterien} verletzen. Planen Sie daher bei jeder Abgabe genügend Zeit ein, mindestens aber einen Tag Puffer. Die Qualitätskriterien werden jeweils im Abschnitt Bearbeitungshinweise beschrieben. Mit fortschreitendem Semester werden die Qualitätskriterien zunehmend strikt.

        \item Der Praktomat ist nur innerhalb des KIT-Netzes erreichbar. Benutzen Sie, wenn Sie Zuhause arbeiten, den VPN-Client des KIT\footnote{\url{http://www.scc.kit.edu/dienste/vpn.php}}.
    \end{itemize}

    \subsection*{Plagiarismus}
    \begin{itemize}

        \item Die Übungsblätter sind selbständig zu bearbeiten. Das Einreichen fremder Lösungen, seien es auch teilweise Lösungen von Dritten, aus Büchern, dem Internet oder anderen Quellen gilt als \textbf{Täuschungsversuch}. Auch die Beihilfe, wie die Weitergabe der eigenen Lösung oder von Teilen davon, wird als Täuschungsversuch gewertet.

        \item Beim ersten Täuschungsversuch wird das komplette Übungsblatt mit 0 Punkten bewertet. Beim zweiten Täuschungsversuch wird der Übungsschein mit \textbf{5,0 bzw. \enquote{nicht bestanden}} bewertet.
    \end{itemize}

    \subsection*{Kommunikation und aktuelle Informationen}
    \begin{itemize}

        \item Fragen zu Vorlesungsinhalten und Übungsblättern stellen Sie bitte ausschließlich in den ILIAS-Foren\footnote{\url{https://ilias.studium.kit.edu/goto.php?target=crs_932855&client_id=produktiv}}.

        So profitieren auch Ihre Kommilitone davon. E-Mails mit inhaltlichen Fragen werden aus genau diesem Grund nicht beantwortet, auch nicht von Tutoren.

        \item In den ILIAS-Foren veröffentlichen wir gelegentlich wichtige Neuigkeiten. Diese sind auch per Twitter\footnote{\url{https://twitter.com/IN1INPROG}}  abonnierbar. Eventuelle Korrekturen von Aufgabenstellungen werden auf diesem Weg bekannt gemacht. Das Beobachten der Neuigkeiten wird daher vorausgesetzt.
    \end{itemize}

    \section*{Termine und Anmeldefristen}
    \begin{itemize}

        \item \textbf{Praktomat}: Die Anmeldung zu Tutorien per \textit{WebInScribe} ist im Sommersemester \textbf{nicht vorgesehen}. Es genügt, sich im Praktomat anzumelden, um einen Tutor zugewiesen zu bekommen. Die erstmalige Anmeldung im Praktomat hat bis \textbf{26.04.2019, 12:00 Uhr} zu erfolgen.

        \item \textbf{Übungschein}: Eine Anmeldung ist zwischen \textbf{24.04.2019 - 23.05.2019 jeweils 12.00 Uhr}  über das Campus Management Portal\footnote{\url{https://campus.studium.kit.edu/}} möglich.

        \item \textbf{Abschlussaufgaben}: Eine Anmeldung ist zwischen \textbf{17.07.2019 - 26.07.2019 jeweils 12.00 Uhr} über das Campus Management Portal möglich.

        \item \textbf{Disclaimer\footnote{\url{https://sdqweb.ipd.kit.edu/disclaimer/}}:} Abgabe erfolgt über den Programmieren-Briefkasten im Gebäude 50.34, 1. Untergeschoss, bis zum \textbf{26.04.2019, 12:00 Uhr}.
    \end{itemize}
}
