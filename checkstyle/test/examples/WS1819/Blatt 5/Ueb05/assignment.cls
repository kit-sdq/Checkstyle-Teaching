\ProvidesClass{assignment}[2013/04/11]
\NeedsTeXFormat{LaTeX2e}
\LoadClass[10pt,a4paper]{article}
\RequirePackage[utf8]{inputenc}
\RequirePackage[T1]{fontenc}
\RequirePackage{ae}
\RequirePackage{kvoptions} % enables key-value style options for classes (absolutely necessary to set e.g. assignment number etc.)

% declare package options
%	  all of these options can be set from outside via kay-value pairs in the document-class declaration
%		the following options exist:
%				- subject: contains the name of the lecture
%				- group: contains the name of the group that organizes the lecture
%				- author: contains a list of people responsible for the lecture
%				- url: contains the url of the lecture website
%				
%				- number: the assignment number
%				- version: the version of the current assignment
%				- start: the start date
%				- end: the end date

\SetupKeyvalOptions {
  family=as,
  prefix=as@
}

% low gradient options
\DeclareStringOption['subject']{subject}
\DeclareStringOption['group']{group}
\DeclareStringOption['author']{author}
\DeclareStringOption['url']{url}
\DeclareStringOption['mail']{mail}

% high gradient options
\DeclareStringOption[<'name']{name}
\DeclareStringOption[1]{number}
\DeclareStringOption[0]{version}
\DeclareStringOption[0]{diff}
\DeclareStringOption[X]{start}
\DeclareStringOption[X + 2]{end}
%\DeclareStringOption[\textit{keine}]{tutorial}

% process package options
\ProcessKeyvalOptions{as}

% Packages needed for this class or regularly used packages
\RequirePackage{eurosym}
\RequirePackage{graphicx}
\RequirePackage{color}
\RequirePackage{xcolor}
\RequirePackage{colortbl}
\RequirePackage{amsmath}
\RequirePackage{amssymb}
\RequirePackage{amsfonts}
\RequirePackage{amsthm}
\RequirePackage{xstring}
\RequirePackage[compact]{titlesec}
\RequirePackage{listings}
\RequirePackage{booktabs}
\RequirePackage{wrapfig}
\RequirePackage{fancyhdr} % pagelayout
\RequirePackage{lastpage} % easily get the number of the last page
\RequirePackage[tikz]{bclogo} % colorful bc-boxes
\RequirePackage{footmisc} % footnotes
\RequirePackage[plainpages=false,pdfpagelabels,pdfborder={0 0 0}]{hyperref} % url and stuff

% set up page stlye (DIN A 4 format, etc.)
\setlength{\textwidth}{17cm}
\setlength{\textheight}{24cm}
\setlength{\topmargin}{-1cm}
\setlength{\oddsidemargin}{-0.5cm}
\setlength{\evensidemargin}{-0.5cm}

\setlength{\parindent}{0cm}
\setlength{\parskip}{1em}
\sloppy

\pagestyle{fancy}%
\lhead{\as@subject}%
\chead{}
\rhead{Abgabe: \as@end}
\renewcommand{\footrulewidth}{0.4pt}
\cfoot{Seite~\thepage~von~\pageref{LastPage}}
\thispagestyle{empty}%

\hypersetup{
  pdftitle=\as@subject\ -\ \as@name,
  pdfauthor=\as@author,
  pdfsubject=\as@subject,
  colorlinks=false,
  pdfborder=0 0 0 0% keine Box um die Links!
}

% Sections are alphanumeric (e.g. part A and B of the assignment)
\renewcommand{\thesection}{\Alph{section}}

% always needed: a todo-macro
\newcommand\todo[1]{\textcolor{red}{#1}}

% display text only if the assignment-number matches
\newcommand\aonly[2]{
  \IfSubStr{#1}{\as@number}{#1}{}
}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% creates a colorful box containing an additional question
\newcommand{\question}[2]{%
	\begin{bclogo}[couleur=red!3!green!15!blue!10!,arrondi=0.1,logo=\bcquestion]{#1}%
		#2%
	\end{bclogo}%
}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% creates a colorful box containing background information
\newcommand{\information}[2]{%
	\begin{bclogo}[couleur=red!70!green!10!,arrondi=0.1,logo=\bcfleur,barre=snake]{#1}%
		#2%
	\end{bclogo}%
}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% create common assignment header
\newcommand{\version}[1]{
  \ifthenelse{\equal{#1}{0}}{}{\ifthenelse{\equal{#1}{1}}{}{Version~#1}}
}
\newcommand{\diff}[1]{
  \ifthenelse{\equal{#1}{0}}{}{Diff~#1}
}

\AtBeginDocument{
  \begin{tabular}{p{\textwidth}}
    \vspace{5pt}
    \begin{minipage}{0.29\textwidth}
      \includegraphics[width=\textwidth]{kit_logo_de_4c_positiv}
    \end{minipage}\hfill
    \begin{minipage}{0.62\textwidth}
      \textbf{\Large \as@subject}\vspace*{.5em}\\
      \as@author\\  
      \as@group\\
      \url{\as@url}\\
      \as@mail
    \end{minipage}\\
    \\  \hline   \\
    \begin{minipage}{0.29\textwidth}
      \centering
      \textbf{\Large \as@name\version{\as@version}~\diff{\as@diff}}
    \end{minipage}\hfill
    \begin{minipage}{0.63\textwidth}
     \begin{tabular}{lr}
     Ausgabe: & \as@start \\ 
     Abgabe: & \as@end \\ 
%     Besprechung: & \as@tutorial \\ 
     \end{tabular} 
    \end{minipage}\\
    \\ \hline
  \end{tabular}\\
}

%%***************************
%% Setting for code listings 
\lstloadlanguages{Java}

\lstset{
  language=Java,
  numbers=left,
  captionpos=b,
  basicstyle=\small \ttfamily,
  commentstyle=\emph,
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

%%***************************
%% Add your own commands
\makeatletter

	\newcommand{\Java}{\textsc{Java}{}}

	\newcommand{\gans}[1] {\glqq #1\grqq}

	\newcommand{\lstinput}[1]{\begin{center} \vspace*{-1em} \begin{minipage}{0.91\linewidth} \begin{center} \lstinputlisting{#1} \end{center} \end{minipage} \vspace*{-1em} \end{center}}

	%\newcommand{\lstline}[1]{\colorbox{gray!30}{\texttt{#1}}}
	
	\newcommand{\lstline}[1]{\setlength{\fboxsep}{2pt}\colorbox{gray!30}{\texttt{#1}}}	
	
	\newenvironment{lstenvironment}{\begin{center} \vspace*{-1em} \begin{minipage}{0.91\textwidth} \begin{lstlisting}}{\end{lstlisting} \end{minipage} \vspace*{-1em} \end{center}}
	
\makeatother
