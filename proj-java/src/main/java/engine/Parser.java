package engine;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/** Classe Parser que faz o parsing da informação contida nos ficheiros .xml
 *  Processa os ficheiros .xml, guardando a informação relevante nas estruturas apropriadas
 *
 * @author Maurício Salgado
 * @author Pedro Machado
 * @author João Coutinho
 * @version 2017/2018
*/

public class Parser {
    /** Map de Posts: usa como key o ID */
    private Map<Long, Post> posts;
    /** Map de Users: usa como key o ID */
    private Map<Long, User> users;
    /** Map de Tags: usa como key a string da Tag */
    private Map<String, Long> tags;
    /** TreeMap de Listas de Posts: usa como key a data do post */
    private TreeMap<LocalDate, ArrayList<Post>> dates;

    /** Construtor do Parser
     *
     */
    public Parser(){
        this.posts = new HashMap<>();
        this.users = new HashMap<>();
        this.tags = new HashMap<>();
        this.dates = new TreeMap<>();
    }

    /** Carrega a informação dos utilizadores em Users.xml na estrutura correspondente
     *
     * @param dumpPath Caminho do ficheiro .xml a ser processado
     * @return  Map: ID de um utilizador -> Instância de User desse utilizador
     */
    public Map<Long, User> loadUsers(String dumpPath){

        String filename = dumpPath.concat("Users.xml");

        Attribute id = null, rep = null, name = null, bio = null;

        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

        try {

            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(filename));

            while(xmlEventReader.hasNext()){

                XMLEvent xmlEvent = xmlEventReader.nextEvent();

                if (xmlEvent.isStartElement()){

                    StartElement startElement = xmlEvent.asStartElement();

                    if(startElement.getName().getLocalPart().equals("row")){

                        id = startElement.getAttributeByName(new QName("Id"));
                        rep = startElement.getAttributeByName(new QName("Reputation"));
                        name = startElement.getAttributeByName(new QName("DisplayName"));
                        bio = startElement.getAttributeByName(new QName("AboutMe"));

                        xmlEvent = xmlEventReader.nextEvent();
                    }
                }

                if(xmlEvent.isEndElement()){
                    EndElement endElement = xmlEvent.asEndElement();

                    User u = new User();

                    u.setID(Long.parseLong(id.getValue()));
                    u.setRep(Integer.parseInt(rep.getValue()));
                    u.setName(name.getValue());
                    if(bio != null) u.setBio(bio.getValue());

                    if(u != null) this.users.put(Long.parseLong(id.getValue()), u);
                }
            }

        } catch (FileNotFoundException | XMLStreamException e) {}

        return this.users;
    }

    /** Carrega a informação dos posts em Posts.xml para a estrutura correspondente
     *
     * @param dumpPath Caminho do ficheiro .xml a ser processado
     * @return  Map: ID de um post -> Instância de Post desse post
     */
    public Map<Long, Post> loadPosts(String dumpPath){

        String filename = dumpPath.concat("Posts.xml");

        int ptype = 0;
        Attribute id = null, post_type = null, date = null, score = null, id_user = null, title = null, answers = null, parentID = null, tag_list = null, cmts = null;
        ArrayList<String> tags = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

        try {
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(filename));
            while(xmlEventReader.hasNext()){
                XMLEvent xmlEvent = xmlEventReader.nextEvent();
                if (xmlEvent.isStartElement()){
                    StartElement startElement = xmlEvent.asStartElement();
                    if(startElement.getName().getLocalPart().equals("row")){

                        id = startElement.getAttributeByName(new QName("Id"));
                        post_type = startElement.getAttributeByName(new QName("PostTypeId"));

                        ptype = Integer.parseInt(post_type.getValue());

                        date = startElement.getAttributeByName(new QName("CreationDate"));
                        score = startElement.getAttributeByName(new QName("Score"));
                        id_user = startElement.getAttributeByName(new QName("OwnerUserId"));
                        title = startElement.getAttributeByName(new QName("Title"));
                        answers = startElement.getAttributeByName(new QName("AnswerCount"));
                        parentID = startElement.getAttributeByName(new QName("ParentId"));
                        tag_list = startElement.getAttributeByName(new QName("Tags"));
                        cmts = startElement.getAttributeByName(new QName( "CommentCount"));

                        xmlEvent = xmlEventReader.nextEvent();
                    }
                }
                if(xmlEvent.isEndElement()){
                    EndElement endElement = xmlEvent.asEndElement();

                    Post p = null;
                    User u = new User();

                    u =  this.users.get(Long.parseLong(id_user.getValue()));

                    if(ptype == 1){

                        p = new Question();

                        p.setId(Long.parseLong(id.getValue()));
                        p.setType(ptype);
                        p.setIdUser(Long.parseLong(id_user.getValue()));
                        p.setScore(Integer.parseInt(score.getValue()));
                        ((Question) p).setTitle(title.getValue());
                        ((Question) p).setAnswers(Integer.parseInt(answers.getValue()));
                        p.setDate(LocalDate.parse(date.getValue(), formatter));
                        p.setRep(this.users.get(Long.parseLong(id_user.getValue())).getRep());

                        StringTokenizer strTkn = new StringTokenizer(tag_list.getValue(), "<>");

                        while(strTkn.hasMoreTokens()){
                            String s = strTkn.nextToken();
                            tags.add(s);
                        }

                        ((Question) p).setTags(tags);

                        this.posts.put(Long.parseLong(id.getValue()), p);

                        u.addPost(p);
                        u.incrementsCount();

                        this.users.put(Long.parseLong(id_user.getValue()), u);

                        tags = new ArrayList<>();
                    }

                    if(ptype == 2){

                        p = new Answer();

                        p.setId(Long.parseLong(id.getValue()));
                        p.setType(ptype);
                        p.setIdUser(Long.parseLong(id_user.getValue()));
                        p.setScore(Integer.parseInt(score.getValue()));
                        p.setDate(LocalDate.parse(date.getValue(), formatter));
                        ((Answer) p).setIdParent(Long.parseLong(parentID.getValue()));
                        ((Answer) p).setComments(Integer.parseInt((cmts.getValue())));
                        p.setRep(this.users.get(Long.parseLong(id_user.getValue())).getRep());

                        this.posts.put(Long.parseLong(id.getValue()), p);

                        u.addPost(p);
                        u.incrementsCount();

                        this.users.put(Long.parseLong(id_user.getValue()), u);
                    }

                    if(p != null){
                        if(this.dates.containsKey(LocalDate.parse(date.getValue(), formatter))){
                            dates.get(LocalDate.parse(date.getValue(), formatter)).add(p);
                        }
                        else{
                            ArrayList<Post> list = new ArrayList<>();
                            list.add(p);
                            dates.put(LocalDate.parse(date.getValue(), formatter),  list);
                        }
                    }
                }
            }
        } catch (FileNotFoundException | XMLStreamException e) {}

        return this.posts;
    }


    /** Carrega a informação das tags do ficheiro Tags.xml na estrutura correspondente
     *
     * @param dumpPath Caminho do ficheiro .xml a ser processado
     * @return  Map: Nome de uma tag -> Instância de Tag dessa tag
     */
    public Map<String, Long> loadTags(String dumpPath){

        String filename = dumpPath.concat("Tags.xml");

        Attribute id = null, name = null;

        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

        try {

            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(filename));

            while(xmlEventReader.hasNext()){

                XMLEvent xmlEvent = xmlEventReader.nextEvent();

                if (xmlEvent.isStartElement()){

                    StartElement startElement = xmlEvent.asStartElement();

                    if(startElement.getName().getLocalPart().equals("row")){


                        id = startElement.getAttributeByName(new QName("Id"));
                        name = startElement.getAttributeByName(new QName("TagName"));

                        xmlEvent = xmlEventReader.nextEvent();
                    }
                }

                if(xmlEvent.isEndElement()){
                    EndElement endElement = xmlEvent.asEndElement();

                    /*
                    Tag t = new Tag();
                    t.setIdTag(Long.parseLong(id.getValue()));
                    t.setTagName(name.toString());
                    */

                    if(name.getValue() != null) this.tags.put(name.getValue(), Long.parseLong(id.getValue()));
                }
            }

        } catch (FileNotFoundException | XMLStreamException e) {}

        return this.tags;
    }

    /** Dá a estrutura TreeMap de todas as datas em que foram feitos posts
     *
     * @return TreeMap: Data -> Lista de posts feitos nessa data
     */
    public TreeMap<LocalDate, ArrayList<Post>> getMapDates(){

        return this.dates;
    }
}
