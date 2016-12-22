# IRC-CID
Model-Controller-View of an IRC client. The model should have to be the most unrelated possible with the controller, so it's possible to define the program behaviour by simply change the controller (and the view).



Current support:
<ul>
<li>All networks supporting full RCF 2812 (i.e. no support for freenode)</li>
<li>SSL connections</li>
<li>Various CTCP requests</li>
<li>All major IRC commands such as 
  <ul>
  <li><code>MODE</code></li>
  <li><code>JOIN</code></li>
  <li><code>QUIT</code></li>
  <li><code>PRIVMSG</code></li>
  <li><code>PART</code></li>
  <li><code>NOTICE</code></li>
  <li><code>NICK</code></li> (planned)
  <li><code>KICK</code></li>
  <li>partial <code>WHOIS</code> support</li>
  <li><code>TOPIC</code></li>
  </ul>
</li>
<li>Port costumizing</li>
</ul>
