% commands for inserting comments
\usepackage{color}
\usepackage[normalem]{ulem} % for \sout (strikethrough o tachado)
\usepackage{ifthen}
\usepackage{comment}
\newboolean{showcomments}
\setboolean{showcomments}{true} % toggle to show or hide comments
\ifthenelse{\boolean{showcomments}}
  {
		%\usepackage{showkeys} %Show labels and refs
		\newcommand{\nbb}[2]{
		% \fbox{\bfseries\sffamily\scriptsize#1}
		\fcolorbox{black}{yellow}{\bfseries\sffamily\scriptsize#1}
		{\sf$\blacktriangleright$\textcolor{blue}{\textit{#2}}$\blacktriangleleft$}
		% \marginpar{\fbox{\bfseries\sffamily#1}}
		}

        \newcommand{\todo}[1]{\textcolor{red}{{[ToDo: #1]}}}
        \newcommand{\internalnote}[1]{\marginpar{\scriptsize note: #1}}
        \newcommand{\version}{\emph{\scriptsize{$-$\today$-$}}}
		\newcommand{\remarks}[1]{\color{red}[#1]\color{black}}
		\newcommand{\modified}[1]{\color{blue}[#1]\color{black}}
		\newcommand{\raw}{$\rightarrow$}
		\newcommand{\ins}[1]{\textcolor{blue}{\uline{#1}}} % please insert
		\newcommand{\del}[1]{\textcolor{red}{\sout{#1}}} % please delete
		\newcommand{\chg}[2]{\textcolor{red}{\sout{#1}}{\raw}\textcolor{blue}{\uline{#2}}} % please change
		\newcommand{\ugh}[1]{\textcolor{red}{\uwave{#1}}} % please rephrase
  }
  {
		\newcommand{\nbb}[2]{}
        \newcommand{\todo}[1]{}
        \newcommand{\internalnote}[1]{}
		\newcommand{\remarks}[1]{}
		\newcommand{\modified}[1]{#1}
		\newcommand{\version}{}
		\newcommand{\ugh}[1]{#1} % please rephrase
		\newcommand{\ins}[1]{#1} % please insert
		\newcommand{\del}[1]{} % please delete
		\newcommand{\chg}[2]{#2} % please change
  }

