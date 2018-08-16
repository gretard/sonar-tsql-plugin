
var http = require('http');
var https = require('https');
var fs = require('fs');
const cheerio = require('cheerio');
var files = [];

if (!fs.existsSync("rules")) {
    fs.mkdirSync("rules");
   downloadFiles();
}

generateDoc();

function add(begin, end, pre) {

    for (var i = 1; i <= end; i++) {
        var s = `${pre}0${i}`;
        if (i < 10) {
            s = `${pre}00${i}`;
        }
        var link = `${begin}${s}`;
        files.push(link);
    }
}




async function downloadFiles() {
    add("https://documentation.red-gate.com/codeanalysis/deprecated-syntax-rules/", 28, "dep");
    add("https://documentation.red-gate.com/codeanalysis/best-practice-rules/", 24, "bp");
    add("https://documentation.red-gate.com/codeanalysis/execution-rules/", 33, "ei");
    add("https://documentation.red-gate.com/codeanalysis/miscellaneous-rules/", 8, "mi");
    add("https://documentation.red-gate.com/codeanalysis/performance-rules/", 23, "pe");
    add("https://documentation.red-gate.com/codeanalysis/script-rules/", 6, "sc");
    add("https://documentation.red-gate.com/codeanalysis/style-rules/", 17, "st");
    files.push("https://documentation.red-gate.com/codeanalysis/miscellaneous-rules/cgunp");
    files.push("https://documentation.red-gate.com/codeanalysis/naming-convention-rules/nc001a");
    files.push("https://documentation.red-gate.com/codeanalysis/naming-convention-rules/nc001d");

    for (var file of files) {
        let names = file.split('/');
        let fileName = names[names.length - 1];
        var request = await downloadFile(file, fileName);
    }

}


async function downloadFile(file, fileName) {
    return https.get(file, function (response) {
        var content = "";
        response.on("data", function (chunk) {
            content += chunk;
        });
        response.on("end", function () {
            let endFile = `./rules/${fileName}.html`;
            fs.writeFileSync(endFile, content, function (err) {
                if (err) {
                    return console.log(err);
                }
                console.log(`The file was saved! ${endFile}`);
            });
        });
    });
}

function generateDoc() {
    var dir = "./rules/";
    var main = "";
    fs.readdirSync("./rules/").forEach(file => {

        var fullFile = `${dir}${file}`;
        var data = fs.readFileSync(fullFile, 'utf8');
        const $ = cheerio.load(data);
        var ruleId = $('h1').last().text().trim().toUpperCase();
        if (ruleId == "NOT FOUND") {
            return;
        }
        var ruleName = $('h2').last().text().trim();
        if (ruleName.length == 0) {
            ruleName = ruleId;
        }
        var txt = $(".grid__col--span-8-of-12 p").toArray()
        if (ruleId == "ST012") {
            ruleName = "Consider using temporary table instead of table variable";
        }
        if (ruleId == "PE009") {
            ruleName = "No SET NOCOUNT ON before DML";
        }
        if (ruleId == "BP022") {
            ruleName ="Money/SmallMoney datatype is used";
        }

        var ruleText = "";
        //        for (var i = 2; i < txt.length - 5; i++) {
        for (var i = 0; i < txt.length; i++) {
            var item = txt[i];
            var text = $(item).text().trim();
            if (text.length==0 || text.startsWith("Didn't find what you were")
            || text.startsWith("Last updated")
            || text.startsWith("SQL Code Guard")
            || text.startsWith("Available in")
            || text.startsWith("(More information coming soon)")
            || text.startsWith("Published")
            || text == ruleName
            || text == ruleId
            || text.startsWith("SQL Prompt")
        ) {
                continue;
            }
            ruleText += "<p>" + text + "</p>";
        }
        var tag = "depreciated";
        var severity = "MAJOR";
        var fixTime = "5min";
        if (ruleId.startsWith("MI")) {
            tag = "misc";
            severity = "MINOR";
            fixTime = "2min";

        }
        if (ruleId.startsWith("ST")) {
            tag = "style";
            severity = "MINOR";
            fixTime = "2min";
        }
        if (ruleId.startsWith("BP")) {
            tag = "best-practice";
            severity = "MINOR";
            fixTime = "3min";
        }
        if (ruleId.startsWith("EI")) {
            tag = "execution-issue";
            severity = "MAJOR";
            fixTime = "5min";
        }
        if (ruleId.startsWith("NC")) {
            tag = "naming";
            severity = "MINOR";
            fixTime = "2min";
        }
        if (ruleId.startsWith("PE")) {
            tag = "performance";
            severity = "MAJOR";
            fixTime = "5min";
        }


        main += `<rule>\r\n`
        main += `<key>${ruleId}</key>\r\n`
        main += `<internalKey>${ruleId}</internalKey>\r\n`
        main += `<name>${ruleName}</name>\r\n`;
        main += `<description><![CDATA[`;
        if (ruleName.length>0) {
            main+= `<h2>${ruleName}</h2>`;
        }
        main += `${ruleText}]]></description>\r\n`;
        main += `<descriptionFormat>HTML</descriptionFormat>\r\n`;
        main += `<severity>${severity}</severity>\r\n`;
        main += `<cardinality>SINGLE</cardinality>\r\n`;
        main += `<status>READY</status>\r\n`;
        main += `<remediationFunction>LINEAR</remediationFunction>\r\n`;
        main += `<remediationFunctionBaseEffort></remediationFunctionBaseEffort>\r\n`;
        main += `<debtRemediationFunctionCoefficient>${fixTime}</debtRemediationFunctionCoefficient>\r\n`;
        main += `<tag>${tag}</tag>\r\n`;
        main += `</rule>\r\n`;

    });
    main = `<sql-rules>${main}</sql-rules>`;
    fs.writeFileSync('rules.xml', main);

}

