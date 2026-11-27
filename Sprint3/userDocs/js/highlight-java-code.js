(function () {
	"use strict";

	var tokenPattern = /("(?:\\.|[^"\\])*"|'(?:\\.|[^'\\])*'|\/\/.*|\/\*[\s\S]*?\*\/|\b\d+(?:\.\d+)?\b|@[A-Za-z_]\w*|\b(?:abstract|assert|boolean|break|byte|case|catch|char|class|const|continue|default|do|double|else|enum|extends|final|finally|float|for|goto|if|implements|import|instanceof|int|interface|long|native|new|null|package|private|protected|public|return|short|static|strictfp|super|switch|synchronized|this|throw|throws|transient|try|void|volatile|while|true|false|var|record|sealed|permits|non-sealed|yield)\b|\b(?:String|System|Integer|Long|Double|Float|Boolean|Object|List|Map|Set|ArrayList|HashMap|Optional|Thread|Runnable|Exception|RuntimeException)\b)/g;

	function tokenClass(token) {
		if (/^\/\//.test(token) || /^\/\*/.test(token)) {
			return "code-comment";
		}

		if (/^["']/.test(token)) {
			return "code-string";
		}

		if (/^@/.test(token)) {
			return "code-annotation";
		}

		if (/^\d/.test(token)) {
			return "code-number";
		}

		if (/^(String|System|Integer|Long|Double|Float|Boolean|Object|List|Map|Set|ArrayList|HashMap|Optional|Thread|Runnable|Exception|RuntimeException)$/.test(token)) {
			return "code-type";
		}

		return "code-keyword";
	}

	function appendText(fragment, text) {
		if (text) {
			fragment.appendChild(document.createTextNode(text));
		}
	}

	function highlight(codeElement) {
		var source = codeElement.textContent;
		var fragment = document.createDocumentFragment();
		var lastIndex = 0;
		var match;

		tokenPattern.lastIndex = 0;

		while ((match = tokenPattern.exec(source)) !== null) {
			appendText(fragment, source.slice(lastIndex, match.index));

			var span = document.createElement("span");
			span.className = tokenClass(match[0]);
			span.textContent = match[0];
			fragment.appendChild(span);

			lastIndex = tokenPattern.lastIndex;
		}

		appendText(fragment, source.slice(lastIndex));
		codeElement.textContent = "";
		codeElement.appendChild(fragment);
		codeElement.classList.add("java-highlighted");
	}

	function highlightAllCodeBlocks() {
		var codeBlocks = document.querySelectorAll("code");

		for (var i = 0; i < codeBlocks.length; i += 1) {
			highlight(codeBlocks[i]);
		}
	}

	if (document.readyState === "loading") {
		document.addEventListener("DOMContentLoaded", highlightAllCodeBlocks);
	} else {
		highlightAllCodeBlocks();
	}
}());
